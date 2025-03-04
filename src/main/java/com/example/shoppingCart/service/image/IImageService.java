package com.example.shoppingCart.service.image;

import com.example.shoppingCart.dto.ImageDto;
import com.example.shoppingCart.model.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteImageById(Long id);
    List<ImageDto> saveImages( Long productId,List<MultipartFile> file );
    void updateImage (MultipartFile file , Long imageId);

}
