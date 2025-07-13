package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImageDto {
    private Long id;
    private String name;
    private String originalFilename;
    private String contentType;
    private Long size;
    private LocalDateTime uploadDate;
    private String downloadUrl;
}