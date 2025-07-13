package com.example.shopapi.service.impl;

import com.example.shopapi.exception.ResourceNotFoundException;
import com.example.shopapi.mapper.ImageMapper;
import com.example.shopapi.model.Image;
import com.example.shopapi.model.ImageDto;
import com.example.shopapi.repository.ImageRepository;
import com.example.shopapi.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    private final Path fileStorageLocation;
    private final ImageRepository imageRepository;
    private final ImageMapper imageMapper;

    public ImageServiceImpl(
            @Value("${file.upload-dir:uploads}") String uploadDir,
            ImageRepository imageRepository,
            ImageMapper imageMapper) throws IOException {
        this.imageRepository = imageRepository;
        this.imageMapper = imageMapper;

        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Files.createDirectories(this.fileStorageLocation);
        log.info("File storage location: {}", this.fileStorageLocation);
    }

    @Override
    public ImageDto uploadImage(MultipartFile file) throws IOException {
        // Normalize file name
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        // Check if the file's name contains invalid characters
        if (originalFilename.contains("..")) {
            throw new IllegalArgumentException("Filename contains invalid path sequence: " + originalFilename);
        }

        // Generate a unique name for the file
        String uniqueFilename = UUID.randomUUID().toString() + "_" + originalFilename;

        // Copy file to the target location
        Path targetLocation = this.fileStorageLocation.resolve(uniqueFilename);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Create and save image metadata
        Image image = Image.builder()
                .name(uniqueFilename)
                .originalFilename(originalFilename)
                .contentType(file.getContentType())
                .path(targetLocation.toString())
                .size(file.getSize())
                .build();

        Image savedImage = imageRepository.save(image);
        log.info("Saved image: {}", savedImage);

        return imageMapper.toDto(savedImage);
    }

    @Override
    public ImageDto getImageByName(String name) {
        Image image = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with name: " + name));
        return imageMapper.toDto(image);
    }

    @Override
    public List<ImageDto> getAllImages() {
        return imageRepository.findAll().stream()
                .map(imageMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Resource loadImageAsResource(String name) {
        try {
            Image image = imageRepository.findByName(name)
                    .orElseThrow(() -> new ResourceNotFoundException("Image not found with name: " + name));

            Path filePath = Paths.get(image.getPath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new ResourceNotFoundException("File not found: " + name);
            }
        } catch (MalformedURLException ex) {
            log.error("Malformed URL for file: {}", ex.getMessage());
            throw new ResourceNotFoundException("File not found: " + name);
        }
    }

    @Override
    public void deleteImage(String name) {
        Image image = imageRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found with name: " + name));

        try {
            Path filePath = Paths.get(image.getPath());
            Files.deleteIfExists(filePath);
            imageRepository.delete(image);
            log.info("Deleted image: {}", image);
        } catch (IOException ex) {
            log.error("Error deleting image file: {}", ex.getMessage());
            throw new RuntimeException("Error deleting image file", ex);
        }
    }
}
