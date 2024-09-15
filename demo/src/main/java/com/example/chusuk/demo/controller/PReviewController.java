package com.example.chusuk.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.service.PReviewService;
import com.example.chusuk.demo.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product/review")
public class PReviewController {
    private final ProductService productService;
    private final PReviewService pReviewService;
    // TODO 제거할까?
}
