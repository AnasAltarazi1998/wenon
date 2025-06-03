package com.example.shopapi.mapper;

import com.example.shopapi.model.User;
import com.example.shopapi.model.UserDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class UserMapper {
    private final ModelMapper modelMapper;
    private final ShopMapper shopMapper;

    public UserMapper(ModelMapper modelMapper, ShopMapper shopMapper) {
        this.modelMapper = modelMapper;
        this.shopMapper = shopMapper;
    }

    public UserDto toDto(User user) {
        UserDto userDto = modelMapper.map(user, UserDto.class);
        if (user.getShops() != null) {
            userDto.setShops(user.getShops().stream()
                .map(shopMapper::toDto)
                .collect(Collectors.toSet()));
        }
        return userDto;
    }

    public User toEntity(UserDto userDto) {
        return modelMapper.map(userDto, User.class);
    }
} 