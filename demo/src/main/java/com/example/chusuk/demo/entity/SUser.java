package com.example.chusuk.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Entity
@ToString
@NoArgsConstructor
public class SUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer Id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    @Column(unique = true)
    private String phoneNumber;
    @Column(unique = true)
    private String eMail;
    private LocalDateTime createDate;
    private Integer userGrade = 0;

    @JsonIgnore
    @OneToOne(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private SCart sCart;
    @JsonIgnore
    @OneToMany(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private List<SAddress> addressList;
    @JsonIgnore
    @OneToMany(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private List<SOrder> orderList;
    @JsonIgnore
    @OneToMany(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private List<PReview> pReviewList;
    @JsonIgnore
    @OneToMany(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private List<QPost> qPostList;
    @JsonIgnore
    @OneToMany(mappedBy = "sUser", cascade = CascadeType.REMOVE)
    private List<QReview> qReviewList;

}
