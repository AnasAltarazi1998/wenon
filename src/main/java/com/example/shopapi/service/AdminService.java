package com.example.shopapi.service;

import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.ContactDto;
import com.example.shopapi.model.LocationDto;

import java.util.List;
import java.util.Set;

public interface AdminService {
    ShopDto createShop(ShopDto shopDto);
    ShopDto updateShop(Long id, ShopDto shopDto);
    void deleteShop(Long id);
    ShopDto updateShopStatus(Long id, String workStatus);
    List<ShopDto> getPendingShops();
    ShopDto getShopById(Long id);
    
    // Bank management methods
    BankDto createBank(BankDto bankDto);
    Set<BankDto> getAllBanks();
    void deleteBank(Long id);
    BankDto updateBank(Long id, BankDto bankDto);
    BankDto getBank(Long id);
    ShopDto updateShopLocation(Long id, LocationDto locationDto);
    ShopDto updateShopContact(Long id, ContactDto contactDto);
    Set<BankDto> updateShopBanks(Long id, Set<BankDto> bankDtos);
    Set<BankDto> deleteShopBanks(Long id, Set<BankDto> bankDtos);
} 