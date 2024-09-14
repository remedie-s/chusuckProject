package com.example.chusuk.demo.service;

import com.example.chusuk.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
}
