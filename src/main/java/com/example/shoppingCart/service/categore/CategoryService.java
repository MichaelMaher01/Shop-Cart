package com.example.shoppingCart.service.categore;

import com.example.shoppingCart.exception.AlreadyExistsException;
import com.example.shoppingCart.exception.ResourceNotFoundException;
import com.example.shoppingCart.model.Category;
import com.example.shoppingCart.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{
    private final CategoryRepository categoryRepository;
    @Override
    public Category getCategoryById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Category Not Found"));
    }

    @Override
    public Category geCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public Category addCategory(Category category) {
        return Optional.of(category).filter(c->!categoryRepository.existsByName(c.getName()))
                .map(categoryRepository::save)
                .orElseThrow(()-> new AlreadyExistsException(category.getName()+"already exists"));
    }

    @Override
    public Category updateCategory(Category category , Long id) {
       return Optional.ofNullable(getCategoryById(id)).map(oldCategory->{
           oldCategory.setName(category.getName());
          return categoryRepository.save(oldCategory);
       } ).orElseThrow(()->new ResourceNotFoundException("Category not found"));
    }

    @Override
    public void deleteCategoryById(Long id) {
         categoryRepository.findById(id)
                .ifPresentOrElse(categoryRepository::delete,()->{
            throw new ResourceNotFoundException("Category not found");
        });

    }
}
