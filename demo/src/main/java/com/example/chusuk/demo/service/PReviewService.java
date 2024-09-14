package com.example.chusuk.demo.service;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.repository.PReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PReviewService {
    private final PReviewRepository pReviewRepository;

}
