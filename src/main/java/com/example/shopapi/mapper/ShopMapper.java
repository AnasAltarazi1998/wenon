package com.example.shopapi.mapper;

import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.Shop;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.UserDto;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    private final ModelMapper modelMapper;
    private final UserMapper userMapper;

    public ShopMapper(ModelMapper modelMapper, UserMapper userMapper) {
        this.modelMapper = modelMapper;
        this.userMapper = userMapper;
    }

    public ShopDto toDto(Shop shop) {
        ShopDto shopDto = modelMapper.map(shop, ShopDto.class);
        shopDto.setBanks(shop.getBanks().stream().map(bank -> modelMapper.map(bank, BankDto.class)).collect(Collectors.toSet()));
        if (shop.getOwner() != null) {
            shopDto.setOwner(userMapper.toDto(shop.getOwner()));
        }
        return shopDto;
    }

    public Shop toEntity(ShopDto shopDto) {
        Shop shop = modelMapper.map(shopDto, Shop.class);
        if (shopDto.getOwner() != null) {
            shop.setOwner(userMapper.toEntity(shopDto.getOwner()));
        }
        return shop;
    }
} 