package com.example.chusuk.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.service.QPostService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qpost")
public class QPostController {
    private final QPostService qPostService;
}
