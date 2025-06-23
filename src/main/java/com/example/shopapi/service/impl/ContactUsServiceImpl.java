package com.example.shopapi.service.impl;

import com.example.shopapi.exception.ResourceNotFoundException;
import com.example.shopapi.mapper.ContactUsMapper;
import com.example.shopapi.model.ContactUs;
import com.example.shopapi.model.ContactUsDto;
import com.example.shopapi.model.ContactUsRequest;
import com.example.shopapi.model.ContactUsStatus;
import com.example.shopapi.repository.ContactUsRepository;
import com.example.shopapi.service.ContactUsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactUsServiceImpl implements ContactUsService {

    private final ContactUsRepository contactUsRepository;
    private final ContactUsMapper contactUsMapper;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    @Transactional
    public ContactUsDto createContactUs(ContactUsRequest request, String username) {
        log.info("Creating new contact request from user: {}", username);
        ContactUs contactUs = contactUsMapper.toEntity(request, username);
        return contactUsMapper.toDto(contactUsRepository.save(contactUs));
    }

    @Override
    public ContactUsDto getContactUsById(Long id) {
        log.info("Getting contact request by id: {}", id);
        return contactUsRepository.findById(id)
                .map(contactUsMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Contact request not found with id: " + id));
    }

    @Override
    public List<ContactUsDto> getAllContactUs() {
        log.info("Getting all contact requests");
        return contactUsRepository.findAll().stream()
                .map(contactUsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactUsDto> getContactUsByUsername(String username) {
        log.info("Getting contact requests for user: {}", username);
        return contactUsRepository.findByUsername(username).stream()
                .map(contactUsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactUsDto> getContactUsByStatus(ContactUsStatus status) {
        log.info("Getting contact requests with status: {}", status);
        return contactUsRepository.findByStatus(status).stream()
                .map(contactUsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ContactUsDto> getContactUsByUsernameAndStatus(String username, ContactUsStatus status) {
        log.info("Getting contact requests for user: {} with status: {}", username, status);
        return contactUsRepository.findByUsernameAndStatus(username, status).stream()
                .map(contactUsMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ContactUsDto updateContactUsStatus(Long id, ContactUsStatus status) {
        log.info("Updating contact request status with id: {} to {}", id, status);
        ContactUs contactUs = contactUsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Contact request not found with id: " + id));
        
        contactUs.setStatus(status);
        contactUs.setUpdatedAt(LocalDateTime.now().format(formatter));
        
        return contactUsMapper.toDto(contactUsRepository.save(contactUs));
    }

    @Override
    @Transactional
    public void deleteContactUs(Long id) {
        log.info("Deleting contact request with id: {}", id);
        if (!contactUsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Contact request not found with id: " + id);
        }
        contactUsRepository.deleteById(id);
    }
}