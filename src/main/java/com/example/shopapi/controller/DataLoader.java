package com.example.shopapi.controller;

import com.example.shopapi.model.*;
import com.example.shopapi.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Arrays;
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
        bankRepository.deleteAll();
        contactRepository.deleteAll();
        locationRepository.deleteAll();
        shopRepository.deleteAll();

        // Create and save locations
        Location riyadhLocation = locationRepository.save(Location.builder()
            .lat(24.7136)
            .lng(46.6753)
            .build());

        Location jeddahLocation = locationRepository.save(Location.builder()
            .lat(21.5433)
            .lng(39.1728)
            .build());

        Location dammamLocation = locationRepository.save(Location.builder()
            .lat(26.4207)
            .lng(50.0888)
            .build());

        // Create and save contacts
        Contact digitalBazaarContact = contactRepository.save(Contact.builder()
            .id(1L)
            .phone("+966501234567")
            .whatsapp("+966501234567")
            .instagram("@digitalbazaar")
            .telegram("@digitalbazaar_support")
            .build());

        Contact techHubContact = contactRepository.save(Contact.builder()
            .id(2L)
            .phone("+966502345678")
            .whatsapp("+966502345678")
            .instagram("@techhub")
            .telegram("@techhub_support")
            .build());

        Contact gadgetWorldContact = contactRepository.save(Contact.builder()
            .id(3L)
            .phone("+966503456789")
            .whatsapp("+966503456789")
            .instagram("@gadgetworld")
            .telegram("@gadgetworld_support")
            .build());

        // Create and save shops
        Shop digitalBazaar = shopRepository.save(Shop.builder()
            .id("shop_12345")
            .name("Digital Bazaar")
            .description("A tech-focused store offering gadgets with cashless payment options.")
            .city("Riyadh")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/digital-bazaar.jpg")
            .openTime("09:00")
            .closeTime("22:00")
            .workStatus("open")
            .contact(digitalBazaarContact)
            .location(riyadhLocation)
            .build());

        Shop techHub = shopRepository.save(Shop.builder()
            .id("shop_23456")
            .name("Tech Hub")
            .description("Your one-stop shop for all tech gadgets and accessories.")
            .city("Jeddah")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/tech-hub.jpg")
            .openTime("08:00")
            .closeTime("21:00")
            .workStatus("open")
            .contact(techHubContact)
            .location(jeddahLocation)
            .build());

        Shop gadgetWorld = shopRepository.save(Shop.builder()
            .id("shop_34567")
            .name("Gadget World")
            .description("Premium electronics and smart home devices.")
            .city("Dammam")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/gadget-world.jpg")
            .openTime("10:00")
            .closeTime("23:00")
            .workStatus("closed")
            .contact(gadgetWorldContact)
            .location(dammamLocation)
            .build());

        // Create and save banks for each shop
        List<Bank> digitalBazaarBanks = Arrays.asList(
            Bank.builder()
                .name("Al Rajhi Bank")
                .imageUrl("https://example.com/banks/al-rajhi.png")
                .shop(digitalBazaar)
                .build(),
            Bank.builder()
                .name("SNB")
                .imageUrl("https://example.com/banks/snb.png")
                .shop(digitalBazaar)
                .build()
        );
        bankRepository.saveAll(digitalBazaarBanks);

        List<Bank> techHubBanks = Arrays.asList(
            Bank.builder()
                .name("Alinma Bank")
                .imageUrl("https://example.com/banks/alinma.png")
                .shop(techHub)
                .build(),
            Bank.builder()
                .name("Al Rajhi Bank")
                .imageUrl("https://example.com/banks/al-rajhi.png")
                .shop(techHub)
                .build()
        );
        bankRepository.saveAll(techHubBanks);

        List<Bank> gadgetWorldBanks = Arrays.asList(
            Bank.builder()
                .name("SNB")
                .imageUrl("https://example.com/banks/snb.png")
                .shop(gadgetWorld)
                .build(),
            Bank.builder()
                .name("Al Bilad Bank")
                .imageUrl("https://example.com/banks/albilad.png")
                .shop(gadgetWorld)
                .build()
        );
        bankRepository.saveAll(gadgetWorldBanks);

        return "success";

    }
}