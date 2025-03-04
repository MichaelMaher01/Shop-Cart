package com.example.shoppingCart.controller;

import com.example.shoppingCart.dto.ImageDto;
import com.example.shoppingCart.exception.ResourceNotFoundException;
import com.example.shoppingCart.model.Image;
import com.example.shoppingCart.response.ApiResponse;
import com.example.shoppingCart.service.image.IImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final IImageService imageService ;
    @PostMapping("/upload")
    public ResponseEntity<ApiResponse> saveImage(@RequestParam List<MultipartFile> files, @RequestParam Long productId){
        try {
            List<ImageDto> imageDtos = imageService.saveImages(productId, files );
            return ResponseEntity.ok(new ApiResponse("upload success",imageDtos));
        } catch (Exception e) {
           return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse("upload failed",e.getMessage()));
        }

    }
    @GetMapping("/image/download/{imageId}")
    public ResponseEntity<Resource> downloadImage (@PathVariable Long imageId) throws SQLException {
        Image image = imageService.getImageById(imageId);
        ByteArrayResource resource= new ByteArrayResource(image.getImage().getBytes(1,(int) image.getImage().length()) );
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(image.getFiletype()))
                .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename=\""+image.getFilename()+"\"")
                .body(resource);
    }
     @PostMapping("image/{imageId}/update")
    public ResponseEntity<ApiResponse> updateImage(@PathVariable Long imageId ,@RequestBody MultipartFile file){
        try {
            Image image= imageService.getImageById(imageId);
           if (image!= null){
               imageService.updateImage(file ,imageId);
               return ResponseEntity.ok(new ApiResponse("Update success",null));
           }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(" Update failed", INTERNAL_SERVER_ERROR));

    }
    @DeleteMapping("image/{imageId}/delete")
    public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId ){
        try {
            Image image= imageService.getImageById(imageId);
            if (image!= null){
                imageService.deleteImageById(imageId);
                return ResponseEntity.ok(new ApiResponse("Update success",null));
            }
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
        return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(" Delete failed", INTERNAL_SERVER_ERROR));

    }

}
