package com.example.shopapi.mapper;

import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.Shop;
import com.example.shopapi.model.ShopDto;

import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ShopMapper {
    private final ModelMapper modelMapper;

    public ShopMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ShopDto toDto(Shop shop) {
        ShopDto shopDto = modelMapper.map(shop, ShopDto.class);
        shopDto.setBanks(shop.getBanks().stream().map(bank -> modelMapper.map(bank, BankDto.class)).collect(Collectors.toSet()));
        return shopDto;
    }

    public Shop toEntity(ShopDto shopDto) {
        return modelMapper.map(shopDto, Shop.class);
    }
} 