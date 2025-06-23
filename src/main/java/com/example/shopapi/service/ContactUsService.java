package com.example.shopapi.service;

import com.example.shopapi.model.ContactUsDto;
import com.example.shopapi.model.ContactUsRequest;
import com.example.shopapi.model.ContactUsStatus;

import java.util.List;

public interface ContactUsService {
    ContactUsDto createContactUs(ContactUsRequest request, String username);
    ContactUsDto getContactUsById(Long id);
    List<ContactUsDto> getAllContactUs();
    List<ContactUsDto> getContactUsByUsername(String username);
    List<ContactUsDto> getContactUsByStatus(ContactUsStatus status);
    List<ContactUsDto> getContactUsByUsernameAndStatus(String username, ContactUsStatus status);
    ContactUsDto updateContactUsStatus(Long id, ContactUsStatus status);
    void deleteContactUs(Long id);
}