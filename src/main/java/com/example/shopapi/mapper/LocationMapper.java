package com.example.shopapi.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.shopapi.model.Location;
import com.example.shopapi.model.LocationDto;

@Component
public class LocationMapper {
    private final ModelMapper modelMapper;

    public LocationMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public LocationDto toDto(Location location) {
        return modelMapper.map(location, LocationDto.class);
    }

    public Location toEntity(LocationDto locationDto) {
        return modelMapper.map(locationDto, Location.class);
    }
}
