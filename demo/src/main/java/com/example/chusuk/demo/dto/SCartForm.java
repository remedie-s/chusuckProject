package com.example.chusuk.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SCartForm {

    private Integer id;
    private LocalDateTime createTime;
    private Integer quantity;
}
