package com.example.shopapi.controller;

import com.example.shopapi.model.ShopDto;
import com.example.shopapi.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shops")
@Slf4j
public class ShopController {

    private final ShopService shopService;

    public ShopController(ShopService shopService) {
        this.shopService = shopService;
    }

    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShopDto>> getAllShops() {
        log.info("Received request to get all shops");
        return ResponseEntity.ok(shopService.getAllShops());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<ShopDto> getShopById(@PathVariable String id) {
        log.info("Received request to get shop by id: {}", id);
        return ResponseEntity.ok(shopService.getShopById(id));
    }

    @GetMapping("/city/{city}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShopDto>> getShopsByCity(@PathVariable String city) {
        log.info("Received request to get shops by city: {}", city);
        return ResponseEntity.ok(shopService.getShopsByCity(city));
    }

    @GetMapping("/category/{category}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShopDto>> getShopsByCategory(@PathVariable String category) {
        log.info("Received request to get shops by category: {}", category);
        return ResponseEntity.ok(shopService.getShopsByCategory(category));
    }

    @GetMapping("/status/{workStatus}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<ShopDto>> getShopsByWorkStatus(@PathVariable String workStatus) {
        log.info("Received request to get shops by work status: {}", workStatus);
        return ResponseEntity.ok(shopService.getShopsByWorkStatus(workStatus));
    }
}