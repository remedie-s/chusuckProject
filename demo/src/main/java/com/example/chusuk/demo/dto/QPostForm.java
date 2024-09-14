package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class QPostForm {
    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;

}
