package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class PReviewForm {
    @NotEmpty
    private String content;

}
