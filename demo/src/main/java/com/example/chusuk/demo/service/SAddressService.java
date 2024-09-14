package com.example.chusuk.demo.service;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.repository.SAddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SAddressService {
    private final SAddressRepository sAddressRepository;
}
