package com.example.shopapi.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import com.example.shopapi.model.ContactUs;
import com.example.shopapi.model.ContactUsDto;
import com.example.shopapi.model.ContactUsRequest;
import com.example.shopapi.model.ContactUsStatus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class ContactUsMapper {
    private final ModelMapper modelMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public ContactUsMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ContactUsDto toDto(ContactUs contactUs) {
        return modelMapper.map(contactUs, ContactUsDto.class);
    }

    public ContactUs toEntity(ContactUsDto contactUsDto) {
        return modelMapper.map(contactUsDto, ContactUs.class);
    }
    
    public ContactUs toEntity(ContactUsRequest request, String username) {
        ContactUs contactUs = new ContactUs();
        contactUs.setTitle(request.getTitle());
        contactUs.setMessage(request.getMessage());
        contactUs.setPhoneNumber(request.getPhoneNumber());
        contactUs.setName(request.getName());
        contactUs.setStatus(ContactUsStatus.PENDING);
        contactUs.setUsername(username);
        
        String now = LocalDateTime.now().format(formatter);
        contactUs.setCreatedAt(now);
        contactUs.setUpdatedAt(now);
        
        return contactUs;
    }
}