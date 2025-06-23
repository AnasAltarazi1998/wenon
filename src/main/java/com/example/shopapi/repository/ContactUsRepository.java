package com.example.shopapi.repository;

import com.example.shopapi.model.ContactUs;
import com.example.shopapi.model.ContactUsStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactUsRepository extends JpaRepository<ContactUs, Long> {
    List<ContactUs> findByUsername(String username);
    List<ContactUs> findByStatus(ContactUsStatus status);
    List<ContactUs> findByUsernameAndStatus(String username, ContactUsStatus status);
}