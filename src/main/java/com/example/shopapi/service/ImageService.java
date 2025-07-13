package com.example.shopapi.service;

import com.example.shopapi.model.ImageDto;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ImageService {
    
    /**
     * Upload an image file
     * 
     * @param file The image file to upload
     * @return The DTO representing the uploaded image
     * @throws IOException If an I/O error occurs
     */
    ImageDto uploadImage(MultipartFile file) throws IOException;
    
    /**
     * Get an image by its name
     * 
     * @param name The name of the image
     * @return The DTO representing the image
     */
    ImageDto getImageByName(String name);
    
    /**
     * Get all images
     * 
     * @return A list of all image DTOs
     */
    List<ImageDto> getAllImages();
    
    /**
     * Load an image as a resource for downloading
     * 
     * @param name The name of the image
     * @return The image resource
     */
    Resource loadImageAsResource(String name);
    
    /**
     * Delete an image by its name
     * 
     * @param name The name of the image
     */
    void deleteImage(String name);
}