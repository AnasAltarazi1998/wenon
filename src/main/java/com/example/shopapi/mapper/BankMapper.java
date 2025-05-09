package com.example.shopapi.mapper;

import com.example.shopapi.model.Bank;
import com.example.shopapi.model.BankDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class BankMapper {
    private final ModelMapper modelMapper;

    public BankMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BankDto toDto(Bank bank) {
        return modelMapper.map(bank, BankDto.class);
    }

    public Bank toEntity(BankDto bankDto) {
        return modelMapper.map(bankDto, Bank.class);
    }

    public Set<Bank> toEntity(Set<BankDto> bankDtos) {
        return bankDtos.stream()
                .map(this::toEntity)
                .collect(Collectors.toSet());
    }

    public void updateEntityFromDto(BankDto bankDto, Bank bank) {
        modelMapper.map(bankDto, bank);
    }
} 