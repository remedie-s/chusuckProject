package com.example.chusuk.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartListDto {

    private Integer id;
    private String imageUrl;
    private String productName;
    private Long price;
    private Integer quantity;
    private Long subtotal;
}
