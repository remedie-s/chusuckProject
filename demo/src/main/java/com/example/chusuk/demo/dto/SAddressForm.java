package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;

public class SAddressForm {

    @NotEmpty
    private String streetName;
    @NotEmpty
    private String buildingNumber;
    @NotEmpty
    private String detailAddress;
    @NotEmpty
    private String city;

}
