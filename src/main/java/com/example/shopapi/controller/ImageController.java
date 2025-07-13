package com.example.shopapi.controller;

import com.example.shopapi.model.ImageDto;
import com.example.shopapi.service.ImageService;
import com.example.shopapi.validator.ImageValidator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Images", description = "Image upload and download APIs")
public class ImageController {

    private final ImageService imageService;
    private final ImageValidator imageValidator;

    @PostMapping
    @Operation(
        summary = "Upload an image",
        description = "Uploads an image file and returns metadata including a download URL"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Image uploaded successfully",
            content = @Content(schema = @Schema(implementation = ImageDto.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid image file"
        ),
        @ApiResponse(
            responseCode = "401",
            description = "Unauthorized"
        )
    })
    public ResponseEntity<ImageDto> uploadImage(@RequestParam("file") MultipartFile file) {
        log.info("Received request to upload image: {}", file.getOriginalFilename());
        
        // Validate the image
        imageValidator.validate(file);
        
        try {
            ImageDto imageDto = imageService.uploadImage(file);
            
            // Build the download URL
            String downloadUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/images/")
                    .path(imageDto.getName())
                    .toUriString();
            
            // Update the download URL in the DTO
            imageDto.setDownloadUrl(downloadUrl);
            
            return ResponseEntity.ok(imageDto);
        } catch (IOException e) {
            log.error("Error uploading image: {}", e.getMessage());
            throw new RuntimeException("Error uploading image", e);
        }
    }

    @GetMapping
    @Operation(
        summary = "Get all images",
        description = "Returns metadata for all uploaded images"
    )
    public ResponseEntity<List<ImageDto>> getAllImages() {
        log.info("Received request to get all images");
        return ResponseEntity.ok(imageService.getAllImages());
    }

    @GetMapping("/{name}")
    @Operation(
        summary = "Download an image",
        description = "Downloads an image by its name"
    )
    public ResponseEntity<Resource> downloadImage(@PathVariable String name) {
        log.info("Received request to download image: {}", name);
        
        Resource resource = imageService.loadImageAsResource(name);
        ImageDto imageDto = imageService.getImageByName(name);
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageDto.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imageDto.getOriginalFilename() + "\"")
                .body(resource);
    }

    @DeleteMapping("/{name}")
    @Operation(
        summary = "Delete an image",
        description = "Deletes an image by its name"
    )
    public ResponseEntity<Void> deleteImage(@PathVariable String name) {
        log.info("Received request to delete image: {}", name);
        imageService.deleteImage(name);
        return ResponseEntity.noContent().build();
    }
}