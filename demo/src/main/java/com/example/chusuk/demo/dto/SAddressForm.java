package com.example.chusuk.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
