package com.example.chusuk.demo.service;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
}
