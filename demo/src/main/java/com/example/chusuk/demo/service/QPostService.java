package com.example.chusuk.demo.service;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.repository.QPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QPostService {

    private final QPostRepository qPostRepository;

}
