package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsDto {
    private Long id;
    private String title;
    private String message;
    private String phoneNumber;
    private String name;
    private ContactUsStatus status;
    private String username;
    private String createdAt;
    private String updatedAt;
}