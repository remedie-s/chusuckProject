package com.example.chusuk.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.service.ProductService;
import com.example.chusuk.demo.service.SUserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class PUserController {
    private final ProductService productService;
    private final SUserService sUserService;

}
