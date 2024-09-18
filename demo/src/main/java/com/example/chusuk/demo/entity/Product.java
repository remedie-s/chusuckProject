package com.example.chusuk.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String productName;
    private String description;
    private Long price;
    private Integer quantity;
    private String imageUrl;
    private LocalDateTime createDate;
    private Integer category;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<PReview> pReviewList;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<PUser> pUserList;
    @JsonIgnore
    @OneToMany(mappedBy = "product", cascade = CascadeType.REMOVE)
    private List<SOrder> sOrderList; // 변경

}
