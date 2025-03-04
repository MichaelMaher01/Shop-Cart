package com.example.shoppingCart.service.product;

import com.example.shoppingCart.dto.ImageDto;
import com.example.shoppingCart.dto.ProductDto;
import com.example.shoppingCart.exception.AlreadyExistsException;
import com.example.shoppingCart.exception.ProductNotFoundException;
import com.example.shoppingCart.model.Category;
import com.example.shoppingCart.model.Image;
import com.example.shoppingCart.model.Product;
import com.example.shoppingCart.repository.CategoryRepository;
import com.example.shoppingCart.repository.ImageRepository;
import com.example.shoppingCart.repository.ProductRepository;
import com.example.shoppingCart.request.AddProductRequest;
import com.example.shoppingCart.request.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductService{

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;
   private    ProductUpdateRequest productUpdateRequest;
    @Override
    public Product addProduct(AddProductRequest request) {

        if (productExists(request.getName(),request.getBrand())){
            throw new AlreadyExistsException(request.getBrand()+" " +request.getName()+" already exists , you may update this product instead!");
        }
       Category category= Optional.ofNullable(categoryRepository.findByName(request.getCategory().getName()))
               .orElseGet(()-> {
                   Category newCategory=new Category(request.getCategory().getName());
                   return categoryRepository.save(newCategory);
               });
       request.setCategory(category);
       return productRepository.save(creatNewProduct(request,category));

    }


    private boolean productExists(String name , String brand ){
        return productRepository.existsByNameAndBrand(name , brand);
    }
    private Product creatNewProduct(AddProductRequest request, Category category){
        return new Product(
                request.getName(),
                request.getBrand(),
                request.getPrice(),
                request.getInventory(),
                request.getDescription(),
                category
        );
    }

    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    @Override
    public void deleteProductById(Long id) {
        productRepository.findById(id)
                .ifPresentOrElse(productRepository::delete,
                ()->{throw new ProductNotFoundException("Product not found");});

    }

    @Override
    public Product updateProduct(ProductUpdateRequest request, Long productId) {
        return productRepository.findById(productId)
                .map(existingPrfoduct -> updateExistingProduct(existingPrfoduct,request))
                .map(productRepository::save)
                .orElseThrow(()-> new ProductNotFoundException("Product not found"));
    }

    private Product updateExistingProduct(Product existingProduct , ProductUpdateRequest request){
        existingProduct.setName(request.getName());
        existingProduct.setBrand(request.getBrand());
        existingProduct.setDescription(request.getDescription());
        existingProduct.setInventory(request.getInventory());
        existingProduct.setPrice(request.getPrice());
        Category category= categoryRepository.findByName(request.getCategory().getName());
        return existingProduct;

    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getProductByCategory(String category) {
        //String formattedName = capitalizeFirstLetter(category);
        return productRepository.findByCategoryName(category);
    }

    @Override
    public List<Product> getProductByBrand(String brand) {
        return productRepository.findByBrand(brand);
    }

    @Override
    public List<Product> getProductByCategoryAndBrand(String category, String brand) {
        return productRepository.findByCategoryNameAndBrand(category,brand);
    }

    @Override
    public List<Product> getProductByName(String name) {
        return productRepository.findByName(name);
    }

    @Override
    public List<Product> getProductByBrandAndName(String brand, String name) {
        return productRepository.findByBrandAndName(brand,name);
    }

    @Override
    public Long countProductByBrandAndName(String brand, String name) {
        return productRepository.countByBrandAndName(brand,name);
    }
    private String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }
    @Override
    public List<ProductDto> getConvertedProducts(List<Product> products){
        return products.stream().map(this::convertToDto).toList();
    }

    @Override
    public ProductDto convertToDto(Product product){
        ProductDto productDto =modelMapper.map(product,ProductDto.class);
        List<Image> images = imageRepository.findByProductId(product.getId());
        List<ImageDto> imageDtos= images.stream()
                .map(image -> modelMapper.map(image,ImageDto.class))
                .toList();
        productDto.setImages(imageDtos);
        return productDto;

    }
}
