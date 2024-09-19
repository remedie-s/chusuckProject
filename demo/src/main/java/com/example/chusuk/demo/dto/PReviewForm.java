package com.example.chusuk.demo.dto;

import com.example.chusuk.demo.entity.Product;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PReviewForm {
    private Product product;
    @NotEmpty
    private String content;

}
