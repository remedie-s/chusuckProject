package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QReviewForm {
    @NotEmpty
    private String content;

}
