package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductForm {

    @NotEmpty
    private String productName;
    @NotEmpty
    private String description;
    private Long price;
    private Integer quantity;
    private Integer category;
    @NotEmpty
    private String imageUrl;

}
