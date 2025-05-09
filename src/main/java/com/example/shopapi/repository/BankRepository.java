package com.example.shopapi.repository;

import com.example.shopapi.model.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {
    boolean existsByName(String name);

} 