package com.example.shopapi.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String password;
    private UserRole role;
    private Set<ShopDto> shops;
    private boolean active;
    @CreationTimestamp
    private String createdAt;
    @UpdateTimestamp
    private String updatedAt;
} 