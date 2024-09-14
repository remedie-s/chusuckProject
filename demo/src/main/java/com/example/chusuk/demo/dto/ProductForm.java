package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class ProductForm {

    @NotEmpty
    private String productName;
    @NotEmpty
    private String description;
    private Long price;
    private Integer quantity;
    @NotEmpty
    private String imageUrl;

}
