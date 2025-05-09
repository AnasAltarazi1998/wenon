package com.example.shopapi.controller;

import com.example.shopapi.model.*;
import com.example.shopapi.repository.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@RestController
@RequestMapping("/api/dataLoader")
public class DataLoader {

    private final ShopRepository shopRepository;
    private final BankRepository bankRepository;
    private final ContactRepository contactRepository;
    private final LocationRepository locationRepository;

    public DataLoader(ShopRepository shopRepository, 
                    BankRepository bankRepository,
                    ContactRepository contactRepository,
                    LocationRepository locationRepository) {
        this.shopRepository = shopRepository;
        this.bankRepository = bankRepository;
        this.contactRepository = contactRepository;
        this.locationRepository = locationRepository;
    }

    @GetMapping("/loadTestData")
    public String loadTestData() {
        // Clear existing data
        shopRepository.deleteAll();
        bankRepository.deleteAll();
        contactRepository.deleteAll();
        locationRepository.deleteAll();
        shopRepository.deleteAll();

        String currentTime = LocalDateTime.now().toString();

        // Create and save locations
        Location riyadhLocation = locationRepository.save(Location.builder()
            .lat(24.7136)
            .lng(46.6753)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Location jeddahLocation = locationRepository.save(Location.builder()
            .lat(21.5433)
            .lng(39.1728)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Location dammamLocation = locationRepository.save(Location.builder()
            .lat(26.4207)
            .lng(50.0888)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        // Create and save contacts
        Contact digitalBazaarContact = contactRepository.save(Contact.builder()
            .id(1L)
            .phone("+966501234567")
            .whatsapp("+966501234567")
            .instagram("@digitalbazaar")
            .telegram("@digitalbazaar_support")
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Contact techHubContact = contactRepository.save(Contact.builder()
            .id(2L)
            .phone("+966502345678")
            .whatsapp("+966502345678")
            .instagram("@techhub")
            .telegram("@techhub_support")
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Contact gadgetWorldContact = contactRepository.save(Contact.builder()
            .id(3L)
            .phone("+966503456789")
            .whatsapp("+966503456789")
            .instagram("@gadgetworld")
            .telegram("@gadgetworld_support")
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        // Create and save banks
        Bank alRajhiBank = bankRepository.save(Bank.builder()
            .name("Al Rajhi Bank")
            .imageUrl("https://example.com/banks/al-rajhi.png")
            .active(true)
            .status("ACTIVE")
            .createdAt(LocalDateTime.now().toString())
            .updatedAt(LocalDateTime.now().toString())
            .build());

        Bank snbBank = bankRepository.save(Bank.builder()
            .name("SNB")
            .imageUrl("https://example.com/banks/snb.png")
            .active(true)
            .status("ACTIVE")
            .createdAt(LocalDateTime.now().toString())
            .updatedAt(LocalDateTime.now().toString())
            .build());

        Bank alinmaBank = bankRepository.save(Bank.builder()
            .name("Alinma Bank")
            .imageUrl("https://example.com/banks/alinma.png")
            .active(true)
            .status("ACTIVE")
            .createdAt(LocalDateTime.now().toString())
            .updatedAt(LocalDateTime.now().toString())
            .build());

        Bank alBiladBank = bankRepository.save(Bank.builder()
            .name("Al Bilad Bank")
            .imageUrl("https://example.com/banks/albilad.png")
            .active(true)
            .status("ACTIVE")
            .createdAt(LocalDateTime.now().toString())
            .updatedAt(LocalDateTime.now().toString())
            .build());

        // Create and save shops with their bank relationships
        Shop digitalBazaar = shopRepository.save(Shop.builder()
            .id(12345L)
            .name("Digital Bazaar")
            .description("A tech-focused store offering gadgets with cashless payment options.")
            .city("Riyadh")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/digital-bazaar.jpg")
            .openTime("09:00")
            .closeTime("22:00")
            .workStatus("OPEN")
            .contact(digitalBazaarContact)
            .location(riyadhLocation)
            .active(true)
            .rating(4.5)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Shop techHub = shopRepository.save(Shop.builder()
            .id(23456L)
            .name("Tech Hub")
            .description("Your one-stop shop for all tech gadgets and accessories.")
            .city("Jeddah")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/tech-hub.jpg")
            .openTime("08:00")
            .closeTime("21:00")
            .workStatus("OPEN")
            .contact(techHubContact)
            .location(jeddahLocation)
            .active(true)
            .rating(4.8)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        Shop gadgetWorld = shopRepository.save(Shop.builder()
            .id(34567L)
            .name("Gadget World")
            .description("Premium electronics and smart home devices.")
            .city("Dammam")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/gadget-world.jpg")
            .openTime("10:00")
            .closeTime("23:00")
            .workStatus("CLOSED")
            .contact(gadgetWorldContact)
            .location(dammamLocation)
            .active(true)
            .rating(4.2)
            .createdAt(currentTime)
            .updatedAt(currentTime)
            .build());

        // Create and save bank relationships
        alRajhiBank.setShops(new HashSet<>(Arrays.asList(digitalBazaar, techHub)));
        snbBank.setShops(new HashSet<>(Arrays.asList(digitalBazaar, techHub)));
        alinmaBank.setShops(new HashSet<>(Arrays.asList(digitalBazaar, techHub)));
        alBiladBank.setShops(new HashSet<>(Arrays.asList(digitalBazaar, techHub,gadgetWorld))); 

        digitalBazaar.setBanks(new HashSet<>(Arrays.asList(alRajhiBank, snbBank, alinmaBank, alBiladBank)));
        techHub.setBanks(new HashSet<>(Arrays.asList(alRajhiBank, snbBank, alinmaBank, alBiladBank)));
        gadgetWorld.setBanks(new HashSet<>(Arrays.asList(alBiladBank)));

        bankRepository.saveAll(Arrays.asList(alRajhiBank, snbBank, alinmaBank, alBiladBank));

        shopRepository.saveAll(Arrays.asList(digitalBazaar, techHub, gadgetWorld));

        return "Test data loaded successfully";
    }
}