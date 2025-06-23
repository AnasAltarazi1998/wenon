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
    private String owner;

    public ShopDto(Long id, String name, String description, String city, String category, String imageUrl,
                  Set<BankDto> banks, String openTime, String closeTime, String workStatus,
                  ContactDto contact, LocationDto location) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.city = city;
        this.category = category;
        this.imageUrl = imageUrl;
        this.banks = banks;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.workStatus = workStatus;
        this.contact = contact;
        this.location = location;
    }
}
