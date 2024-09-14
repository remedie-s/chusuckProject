package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class QReviewForm {
    @NotEmpty
    private String content;

}
