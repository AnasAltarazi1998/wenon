package com.example.shopapi.controller;

import com.example.shopapi.model.ContactUsDto;
import com.example.shopapi.model.ContactUsRequest;
import com.example.shopapi.model.ContactUsStatus;
import com.example.shopapi.service.ContactUsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-us")
@RequiredArgsConstructor
@Slf4j
public class ContactUsController {

    private final ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<ContactUsDto> createContactUs(@RequestBody ContactUsRequest request, Authentication authentication) {
        log.info("Received request to create new contact request");
        String username = authentication.getName();
        return ResponseEntity.ok(contactUsService.createContactUs(request, username));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @contactUsService.getContactUsById(#id).username == authentication.name")
    public ResponseEntity<ContactUsDto> getContactUsById(@PathVariable Long id) {
        log.info("Received request to get contact request by id: {}", id);
        return ResponseEntity.ok(contactUsService.getContactUsById(id));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactUsDto>> getAllContactUs() {
        log.info("Received request to get all contact requests");
        return ResponseEntity.ok(contactUsService.getAllContactUs());
    }

    @GetMapping("/my-requests")
    public ResponseEntity<List<ContactUsDto>> getMyContactUs(Authentication authentication) {
        log.info("Received request to get contact requests for current user");
        String username = authentication.getName();
        return ResponseEntity.ok(contactUsService.getContactUsByUsername(username));
    }

    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ContactUsDto>> getContactUsByStatus(@PathVariable ContactUsStatus status) {
        log.info("Received request to get contact requests with status: {}", status);
        return ResponseEntity.ok(contactUsService.getContactUsByStatus(status));
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ContactUsDto> updateContactUsStatus(@PathVariable Long id, @RequestParam ContactUsStatus status) {
        log.info("Received request to update contact request status with id: {} to {}", id, status);
        return ResponseEntity.ok(contactUsService.updateContactUsStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteContactUs(@PathVariable Long id) {
        log.info("Received request to delete contact request with id: {}", id);
        contactUsService.deleteContactUs(id);
        return ResponseEntity.noContent().build();
    }
}