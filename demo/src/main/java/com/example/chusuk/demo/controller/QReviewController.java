package com.example.chusuk.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.service.QPostService;
import com.example.chusuk.demo.service.QReviewService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qpost/review")
public class QReviewController {
    private final QPostService qPostService;
    private final QReviewService qReviewService;

}
