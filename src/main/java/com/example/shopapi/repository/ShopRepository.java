package com.example.shopapi.repository;

import com.example.shopapi.model.Shop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShopRepository extends JpaRepository<Shop, String>, JpaSpecificationExecutor<Shop> {
    List<Shop> findByCity(String city);
    List<Shop> findByCategory(String category);
    List<Shop> findByWorkStatus(String workStatus);
}