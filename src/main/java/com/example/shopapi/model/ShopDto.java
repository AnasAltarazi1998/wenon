package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {
    private Long id;
    private String name;
    private String description;
    private String city;
    private String category;
    private String imageUrl;
    private Set<BankDto> banks;
    private String openTime;
    private String closeTime;
    private String workStatus;
    private ContactDto contact;
    private LocationDto location;
}