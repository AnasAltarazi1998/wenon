package com.example.shopapi.service.impl;

import com.example.shopapi.mapper.ShopMapper;
import com.example.shopapi.exception.ResourceNotFoundException;
import com.example.shopapi.mapper.BankMapper;
import com.example.shopapi.mapper.ContactMapper;
import com.example.shopapi.mapper.LocationMapper;
import com.example.shopapi.mapper.UserMapper;
import com.example.shopapi.model.Shop;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.Bank;
import com.example.shopapi.model.BankDto;
import com.example.shopapi.model.Contact;
import com.example.shopapi.model.ContactDto;
import com.example.shopapi.model.Location;
import com.example.shopapi.model.LocationDto;
import com.example.shopapi.model.User;
import com.example.shopapi.repository.ShopRepository;
import com.example.shopapi.repository.BankRepository;
import com.example.shopapi.repository.UserRepository;
import com.example.shopapi.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final ShopRepository shopRepository;
    private final ShopMapper shopMapper;
    private final BankRepository bankRepository;
    private final BankMapper bankMapper;
    private final LocationMapper locationMapper;
    private final ContactMapper contactMapper;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public ShopDto createShop(ShopDto shopDto) {
        log.info("Creating new shop: {}", shopDto);

        // Set default values if not provided
        if (shopDto.getBanks() == null) {
            shopDto.setBanks(new HashSet<>());
        }

        if (shopDto.getWorkStatus() == null) {
            shopDto.setWorkStatus("PENDING");
        }

        Shop shop = shopMapper.toEntity(shopDto);

        // Set initial values
        shop.setActive(true);
        shop.setCreatedAt(LocalDateTime.now().toString());
        shop.setUpdatedAt(LocalDateTime.now().toString());

        return shopMapper.toDto(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public ShopDto updateShop(Long id, ShopDto shopDto) {
        log.info("Updating shop with id: {}", id);
        if (!shopRepository.existsById(id)) {
            throw new RuntimeException("Shop not found with id: " + id);
        }
        Shop shop = shopMapper.toEntity(shopDto);
        shop.setUpdatedAt(LocalDateTime.now().toString());
        shop.setBanks(new HashSet<>(bankMapper.toEntity(shopDto.getBanks())));
        shop.setId(id);
        return shopMapper.toDto(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public void deleteShop(Long id) {
        log.info("Deleting shop with id: {}", id);
        if (!shopRepository.existsById(id)) {
            throw new RuntimeException("Shop not found with id: " + id);
        }
        shopRepository.deleteById(id);
    }

    @Override
    @Transactional
    public ShopDto updateShopStatus(Long id, String workStatus) {
        log.info("Updating shop status for id: {} to: {}", id, workStatus);
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shop not found with id: " + id));
        shop.setWorkStatus(workStatus);
        return shopMapper.toDto(shopRepository.save(shop));
    }

    @Override
    public List<ShopDto> getPendingShops() {
        log.info("Getting all pending shops");
        return shopRepository.findAll().stream()
                .filter(shop -> "PENDING".equals(shop.getWorkStatus()))
                .map(shopMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ShopDto getShopById(Long id) {
        log.info("Getting shop by id: {}", id);
        return shopRepository.findById(id)
                .map(shopMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
    }

    @Override
    @Transactional
    public ShopDto updateShopLocation(Long id, LocationDto locationDto) {
        log.info("Updating shop location for id: {}", id);
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
        shop.setLocation(locationMapper.toEntity(locationDto));
        return shopMapper.toDto(shopRepository.save(shop));
    }   

    @Override
    @Transactional
    public ShopDto updateShopContact(Long id, ContactDto contactDto) {
        log.info("Updating shop contact for id: {}", id);
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
        shop.setContact(contactMapper.toEntity(contactDto));
        return shopMapper.toDto(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public Set<BankDto> updateShopBanks(Long id, Set<BankDto> bankDtos) {
        log.info("Updating shop banks for id: {}", id);
        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
        Set<Bank> banks = new HashSet<>(bankRepository.findAllById(bankDtos.stream().map(BankDto::getId).collect(Collectors.toSet())));
        shop.setBanks(banks);
        banks.forEach(bank -> bank.getShops().add(shop));
        shopRepository.save(shop);
        bankRepository.saveAll(banks);
        return bankDtos;
    }

    @Override
    @Transactional
    public Set<BankDto> deleteShopBanks(Long id, Set<BankDto> bankDtos) {
        log.info("Deleting shop banks for id: {}", id);

        Shop shop = shopRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + id));
       if(bankDtos.isEmpty()) {
            shop.getBanks().clear();
            shopRepository.save(shop);
            return bankDtos;
        }
        Set<Bank> banks = new HashSet<>(bankRepository.findAllById(bankDtos.stream().map(BankDto::getId).collect(Collectors.toSet())));
        shop.getBanks().removeAll(banks);
        banks.forEach(bank -> bank.getShops().remove(shop));
        shopRepository.save(shop);
        bankRepository.saveAll(banks);
        return bankDtos;
    }   

    @Override
    @Transactional
    public BankDto createBank(BankDto bankDto) {
        log.info("Creating new bank: {}", bankDto.getName());

        Bank bank = bankMapper.toEntity(bankDto);

        // Set initial values
        bank.setActive(true);
        bank.setStatus("ACTIVE");
        bank.setCreatedAt(LocalDateTime.now().toString());
        bank.setUpdatedAt(LocalDateTime.now().toString());

        Bank savedBank = bankRepository.save(bank);
        log.info("Successfully created bank with id: {}", savedBank.getId());

        return bankMapper.toDto(savedBank);
    }

    @Override
    public Set<BankDto> getAllBanks() {
        log.info("Getting all banks");
        return bankRepository.findAll().stream()
                .map(bankMapper::toDto)
                .collect(Collectors.toSet());
    }

    @Override
    public void deleteBank(Long id) {
        log.info("Deleting bank with id: {}", id);
        bankRepository.deleteById(id);
    }

    @Override
    @Transactional
    public BankDto updateBank(Long id, BankDto bankDto) {
        log.info("Updating bank with id: {}", id);
        Bank bank = bankRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with id: " + id));
        bankMapper.updateEntityFromDto(bankDto, bank);
        bank.setUpdatedAt(LocalDateTime.now().toString());
        return bankMapper.toDto(bankRepository.save(bank));
    }

    @Override
    public BankDto getBank(Long id) {
        log.info("Getting bank with id: {}", id);
        return bankRepository.findById(id)
                .map(bankMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with id: " + id));
    }

    @Override
    @Transactional
    public ShopDto addShopToUser(Long userId, Long shopId) {
        log.info("Adding shop with id: {} to user with id: {}", shopId, userId);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        shop.setOwner(user);
        shop.setUpdatedAt(LocalDateTime.now().toString());

        if (user.getShops() == null) {
            user.setShops(new HashSet<>());
        }
        user.getShops().add(shop);

        shopRepository.save(shop);
        userRepository.save(user);

        return shopMapper.toDto(shop);
    }

    @Override
    @Transactional
    public BankDto addBankToShop(Long shopId, Long bankId) {
        log.info("Adding bank with id: {} to shop with id: {}", bankId, shopId);

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Bank not found with id: " + bankId));

        if (shop.getBanks() == null) {
            shop.setBanks(new HashSet<>());
        }
        shop.getBanks().add(bank);

        if (bank.getShops() == null) {
            bank.setShops(new HashSet<>());
        }
        bank.getShops().add(shop);

        shop.setUpdatedAt(LocalDateTime.now().toString());
        bank.setUpdatedAt(LocalDateTime.now().toString());

        shopRepository.save(shop);
        bankRepository.save(bank);

        return bankMapper.toDto(bank);
    }

    @Override
    @Transactional
    public ShopDto addContactToShop(Long shopId, ContactDto contactDto) {
        log.info("Adding contact to shop with id: {}", shopId);

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        Contact contact = contactMapper.toEntity(contactDto);
        contact.setCreatedAt(LocalDateTime.now().toString());
        contact.setUpdatedAt(LocalDateTime.now().toString());

        shop.setContact(contact);
        shop.setUpdatedAt(LocalDateTime.now().toString());

        return shopMapper.toDto(shopRepository.save(shop));
    }

    @Override
    @Transactional
    public ShopDto addLocationToShop(Long shopId, LocationDto locationDto) {
        log.info("Adding location to shop with id: {}", shopId);

        Shop shop = shopRepository.findById(shopId)
                .orElseThrow(() -> new ResourceNotFoundException("Shop not found with id: " + shopId));

        Location location = locationMapper.toEntity(locationDto);
        location.setCreatedAt(LocalDateTime.now().toString());
        location.setUpdatedAt(LocalDateTime.now().toString());

        shop.setLocation(location);
        shop.setUpdatedAt(LocalDateTime.now().toString());

        return shopMapper.toDto(shopRepository.save(shop));
    }
} 
