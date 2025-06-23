package com.example.shopapi.controller;

import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.ContactDto;
import com.example.shopapi.model.LocationDto;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@Slf4j
public class AdminController {

    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping("/shops")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopDto> createShop(@RequestBody ShopDto shopDto) {
        log.info("Received request to create new shop");
        return ResponseEntity.ok(adminService.createShop(shopDto));
    }

    @PutMapping("/shops/{id}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<ShopDto> updateShop(@PathVariable Long id, @RequestBody ShopDto shopDto) {
        log.info("Received request to update shop with id: {}", id);
        return ResponseEntity.ok(adminService.updateShop(id, shopDto));
    }

    @DeleteMapping("/shops/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteShop(@PathVariable Long id) {
        log.info("Received request to delete shop with id: {}", id);
        adminService.deleteShop(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/shops/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<ShopDto> updateShopStatus(@PathVariable Long id, @RequestParam String workStatus) {
        log.info("Received request to update shop status for id: {} to: {}", id, workStatus);
        return ResponseEntity.ok(adminService.updateShopStatus(id, workStatus));
    }

    @GetMapping("/shops/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ShopDto>> getPendingShops() {
        log.info("Received request to get all pending shops");
        return ResponseEntity.ok(adminService.getPendingShops());
    }

    @PutMapping("/shops/{id}/location")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<ShopDto> updateShopLocation(@PathVariable Long id, @RequestBody LocationDto locationDto) {
        log.info("Received request to update shop location for id: {}", id);
        return ResponseEntity.ok(adminService.updateShopLocation(id, locationDto));
    }

    @PutMapping("/shops/{id}/contact")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<ShopDto> updateShopContact(@PathVariable Long id, @RequestBody ContactDto contactDto) {
        log.info("Received request to update shop contact for id: {}", id);
        return ResponseEntity.ok(adminService.updateShopContact(id, contactDto));
    }

    @PutMapping("/shops/{id}/banks")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<Set<BankDto>> createBank(@PathVariable Long id, @RequestBody Set<BankDto> bankDtos) {
        log.info("Received request to create new bank for shop with id: {}", id);
        return ResponseEntity.ok(adminService.updateShopBanks(id, bankDtos));
    }

    @DeleteMapping("/shops/{id}/banks")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#id).owner.username == authentication.name)")
    public ResponseEntity<Set<BankDto>> deleteBank(@PathVariable Long id, @RequestBody Set<BankDto> bankDtos) {
        log.info("Received request to delete bank for shop with id: {}", id);
        return ResponseEntity.ok(adminService.deleteShopBanks(id, bankDtos));
    }

    @PostMapping("/banks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankDto> createBank(@RequestBody BankDto bankDto) {
        log.info("Received request to create new bank");
        return ResponseEntity.ok(adminService.createBank(bankDto));
    }

    @GetMapping("/banks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Set<BankDto>> getAllBanks() {
        log.info("Received request to get all banks");
        return ResponseEntity.ok(adminService.getAllBanks());
    }   

    @DeleteMapping("/banks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteBank(@PathVariable Long id) {
        log.info("Received request to delete bank with id: {}", id);
        adminService.deleteBank(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/banks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankDto> updateBank(@PathVariable Long id, @RequestBody BankDto bankDto) {
        log.info("Received request to update bank with id: {}", id);
        return ResponseEntity.ok(adminService.updateBank(id, bankDto));
    }

    @GetMapping("/banks/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankDto> getBank(@PathVariable Long id) {
        log.info("Received request to get bank with id: {}", id);
        return ResponseEntity.ok(adminService.getBank(id));
    }

    @PostMapping("/users/{userId}/shops/{shopId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ShopDto> addShopToUser(@PathVariable Long userId, @PathVariable Long shopId) {
        log.info("Received request to add shop with id: {} to user with id: {}", shopId, userId);
        return ResponseEntity.ok(adminService.addShopToUser(userId, shopId));
    }

    @PostMapping("/shops/{shopId}/banks/{bankId}")
    @PreAuthorize("hasRole('ADMIN') or (hasRole('SHOP_OWNER') and @adminService.getShopById(#shopId).owner.username == authentication.name)")
    public ResponseEntity<BankDto> addBankToShop(@PathVariable Long shopId, @PathVariable Long bankId) {
        log.info("Received request to add bank with id: {} to shop with id: {}", bankId, shopId);
        return ResponseEntity.ok(adminService.addBankToShop(shopId, bankId));
    }
}
