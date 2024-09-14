package com.example.chusuk.demo.service;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.repository.SCartRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SCartService {
    private final SCartRepository sCartRepository;
}
