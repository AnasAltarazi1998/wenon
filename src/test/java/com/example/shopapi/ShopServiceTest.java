package com.example.shopapi;

import com.example.shopapi.model.Shop;
import com.example.shopapi.model.ShopDto;
import com.example.shopapi.model.*;
import com.example.shopapi.repository.ShopRepository;
import com.example.shopapi.service.ShopService;
import com.example.shopapi.exception.ResourceNotFoundException;
import com.example.shopapi.mapper.ShopMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ShopServiceTest {

    @Mock
    private ShopRepository shopRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ShopMapper shopMapper;

    @InjectMocks
    private ShopService shopService;

    private Shop shop;
    private ShopDto shopDto;
    private Set<Bank> banks;
    private Contact contact;
    private Location location;
    private ModelMapper testMapper;

    @BeforeEach
    void setUp() {
        testMapper = new ModelMapper();

        // Setup test data
        banks = new HashSet<>(Arrays.asList(
            Bank.builder()
                .id(1L)
                .name("Al Rajhi Bank")
                .imageUrl("https://example.com/banks/al-rajhi.png")
                .build(),
            Bank.builder()
                .id(2L)
                .name("SNB")
                .imageUrl("https://example.com/banks/snb.png")
                .build()
        ));

        contact = Contact.builder()
            .id(1L)
            .phone("+966501234567")
            .whatsapp("+966501234567")
            .instagram("@digitalbazaar")
            .telegram("@digitalbazaar_support")
            .build();

        location = Location.builder()
            .id(1L)
            .lat(24.7136)
            .lng(46.6753)
            .build();

        shop = Shop.builder()
            .id(12345L)
            .name("Digital Bazaar")
            .description("A tech-focused store offering gadgets with cashless payment options.")
            .city("Riyadh")
            .category("Electronics")
            .imageUrl("https://example.com/images/shops/digital-bazaar.jpg")
            .banks(new HashSet<>(banks))
            .openTime("09:00")
            .closeTime("22:00")
            .workStatus("open")
            .contact(contact)
            .location(location)
            .build();

        shopDto = new ShopDto(
            12345L,
            "Digital Bazaar",
            "A tech-focused store offering gadgets with cashless payment options.",
            "Riyadh",
            "Electronics",
            "https://example.com/images/shops/digital-bazaar.jpg",
            banks.stream().map(bank -> testMapper.map(bank, BankDto.class)).collect(Collectors.toSet()),
            "09:00",
            "22:00",
            "open",
            testMapper.map(contact, ContactDto.class),
            testMapper.map(location, LocationDto.class)
        );
    }

    @Test
    void getAllShops_ShouldReturnAllShops() {
        // Arrange
        List<Shop> shops = Collections.singletonList(shop);
        when(shopRepository.findAll()).thenReturn(shops);
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        List<ShopDto> result = shopService.getAllShops();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(12345L, result.get(0).getId());
        verify(shopRepository).findAll();
        verify(shopMapper).toDto(shop);
    }

    @Test
    void getAllShops_WhenEmpty_ShouldReturnEmptyList() {
        // Arrange
        when(shopRepository.findAll()).thenReturn(Collections.emptyList());
        // No need to mock shopMapper.toDto since the list is empty

        // Act
        List<ShopDto> result = shopService.getAllShops();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shopRepository).findAll();
        // No need to verify shopMapper.toDto since it's not called with an empty list
    }

    @Test
    void getShopById_WhenExists_ShouldReturnShop() {
        // Arrange
        when(shopRepository.findById(12345L)).thenReturn(Optional.of(shop));
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        ShopDto result = shopService.getShopById(12345L);

        // Assert
        assertNotNull(result);
        assertEquals("Digital Bazaar", result.getName());
        assertEquals("Riyadh", result.getCity());
        assertEquals("Electronics", result.getCategory());
        assertEquals("open", result.getWorkStatus());
        verify(shopRepository).findById(12345L);
        verify(shopMapper).toDto(shop);
    }

    @Test
    void getShopById_WhenNotExists_ShouldThrowException() {
        // Arrange
        when(shopRepository.findById(12345L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            shopService.getShopById(12345L);
        });
        verify(shopRepository).findById(12345L);
    }

    @Test
    void getShopsByCity_ShouldReturnFilteredShops() {
        // Arrange
        List<Shop> shops = Arrays.asList(shop);
        when(shopRepository.findByCity("Riyadh")).thenReturn(shops);
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        List<ShopDto> result = shopService.getShopsByCity("Riyadh");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Riyadh", result.get(0).getCity());
        verify(shopRepository).findByCity("Riyadh");
        verify(shopMapper).toDto(shop);
    }

    @Test
    void getShopsByCity_WhenNoShops_ShouldReturnEmptyList() {
        // Arrange
        when(shopRepository.findByCity("Unknown")).thenReturn(Collections.emptyList());

        // Act
        List<ShopDto> result = shopService.getShopsByCity("Unknown");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shopRepository).findByCity("Unknown");
    }

    @Test
    void getShopsByCategory_ShouldReturnFilteredShops() {
        // Arrange
        List<Shop> shops = Arrays.asList(shop);
        when(shopRepository.findByCategory("Electronics")).thenReturn(shops);
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        List<ShopDto> result = shopService.getShopsByCategory("Electronics");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
        verify(shopRepository).findByCategory("Electronics");
        verify(shopMapper).toDto(shop);
    }

    @Test
    void getShopsByCategory_WhenNoShops_ShouldReturnEmptyList() {
        // Arrange
        when(shopRepository.findByCategory("Unknown")).thenReturn(Collections.emptyList());

        // Act
        List<ShopDto> result = shopService.getShopsByCategory("Unknown");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shopRepository).findByCategory("Unknown");
    }

    @Test
    void getShopsByWorkStatus_ShouldReturnFilteredShops() {
        // Arrange
        List<Shop> shops = Arrays.asList(shop);
        when(shopRepository.findByWorkStatus("open")).thenReturn(shops);
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        List<ShopDto> result = shopService.getShopsByWorkStatus("open");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("open", result.get(0).getWorkStatus());
        verify(shopRepository).findByWorkStatus("open");
        verify(shopMapper).toDto(shop);
    }

    @Test
    void getShopsByWorkStatus_WhenNoShops_ShouldReturnEmptyList() {
        // Arrange
        when(shopRepository.findByWorkStatus("closed")).thenReturn(Collections.emptyList());

        // Act
        List<ShopDto> result = shopService.getShopsByWorkStatus("closed");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(shopRepository).findByWorkStatus("closed");
    }

    @Test
    void convertToDto_ShouldReturnCorrectDto() {
        // Arrange
        when(shopMapper.toDto(shop)).thenReturn(shopDto);

        // Act
        ShopDto result = shopService.convertToDto(shop);

        // Assert
        assertNotNull(result);
        assertEquals(shop.getId(), result.getId());
        assertEquals(shop.getName(), result.getName());
        assertEquals(shop.getDescription(), result.getDescription());
        assertEquals(shop.getCity(), result.getCity());
        assertEquals(shop.getCategory(), result.getCategory());
        assertEquals(shop.getImageUrl(), result.getImageUrl());
        assertEquals(shop.getOpenTime(), result.getOpenTime());
        assertEquals(shop.getCloseTime(), result.getCloseTime());
        assertEquals(shop.getWorkStatus(), result.getWorkStatus());
        assertEquals(shop.getBanks().stream().map(bank -> testMapper.map(bank, BankDto.class)).collect(Collectors.toSet()), result.getBanks());
        assertEquals(testMapper.map(shop.getContact(), ContactDto.class), result.getContact());
        assertEquals(testMapper.map(shop.getLocation(), LocationDto.class), result.getLocation());
        verify(shopMapper).toDto(shop);
    }

    @Test
    void convertToEntity_ShouldReturnCorrectEntity() {
        // Arrange
        when(shopMapper.toEntity(shopDto)).thenReturn(shop);

        // Act
        Shop result = shopService.convertToEntity(shopDto);

        // Assert
        assertNotNull(result);
        assertEquals(shopDto.getId(), result.getId());
        assertEquals(shopDto.getName(), result.getName());
        assertEquals(shopDto.getDescription(), result.getDescription());
        assertEquals(shopDto.getCity(), result.getCity());
        assertEquals(shopDto.getCategory(), result.getCategory());
        assertEquals(shopDto.getImageUrl(), result.getImageUrl());
        assertEquals(shopDto.getOpenTime(), result.getOpenTime());
        assertEquals(shopDto.getCloseTime(), result.getCloseTime());
        assertEquals(shopDto.getWorkStatus(), result.getWorkStatus());
        assertEquals(shopDto.getBanks(), result.getBanks().stream().map(bank -> testMapper.map(bank, BankDto.class)).collect(Collectors.toSet()));
        assertEquals(shopDto.getContact(), testMapper.map(result.getContact(), ContactDto.class));
        assertEquals(shopDto.getLocation(), testMapper.map(result.getLocation(), LocationDto.class));
        verify(shopMapper).toEntity(shopDto);
    }

    @Test
    void convertToDto_WhenNull_ShouldReturnNull() {
        // Act
        ShopDto result = shopService.convertToDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void convertToEntity_WhenNull_ShouldReturnNull() {
        // Act
        Shop result = shopService.convertToEntity(null);

        // Assert
        assertNull(result);
    }
}
