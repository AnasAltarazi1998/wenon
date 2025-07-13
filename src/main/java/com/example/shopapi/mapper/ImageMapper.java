package com.example.shopapi.mapper;

import com.example.shopapi.model.Image;
import com.example.shopapi.model.ImageDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ImageMapper {

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    public ImageDto toDto(Image image) {
        if (image == null) {
            return null;
        }

        return ImageDto.builder()
                .id(image.getId())
                .name(image.getName())
                .originalFilename(image.getOriginalFilename())
                .contentType(image.getContentType())
                .size(image.getSize())
                .uploadDate(image.getUploadDate())
                .downloadUrl("/api/images/" + image.getName())
                .build();
    }

    public Image toEntity(ImageDto imageDto) {
        if (imageDto == null) {
            return null;
        }

        return Image.builder()
                .id(imageDto.getId())
                .name(imageDto.getName())
                .originalFilename(imageDto.getOriginalFilename())
                .contentType(imageDto.getContentType())
                .size(imageDto.getSize())
                .build();
    }
}