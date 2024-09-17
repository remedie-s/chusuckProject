package com.example.chusuk.demo.dto;

import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.entity.SOrder;
import com.example.chusuk.demo.entity.SUser;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListDto {

    private SUser sUser;
    private Product product;
    private SOrder sOrder;
    private Long subtotal;

}
