package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QPostForm {
    @NotEmpty
    private String subject;
    @NotEmpty
    private String content;

}
