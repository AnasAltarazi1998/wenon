package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactUsRequest {
    private String title;
    private String message;
    private String phoneNumber;
    private String name;
}