package com.example.chusuk.demo.service;

import java.time.DateTimeException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.SAddress;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.repository.SAddressRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SAddressService {
    private final SAddressRepository sAddressRepository;

    public SAddress getoneAddress(Integer id) {

        Optional<SAddress> byId = this.sAddressRepository.findById(id);
        if (byId.isPresent()) {
            SAddress sAddress = byId.get();
            return sAddress;
        }
        throw new DateTimeException("주소가 없어요");

    }

    public SAddress create(SUser user, String streetName, String buildingNumber, String detailAddress, String city) {
        SAddress sAddress = new SAddress();
        sAddress.setSUser(user);
        sAddress.setStreetName(streetName);
        sAddress.setBuildingNumber(buildingNumber);
        sAddress.setDetailAddress(detailAddress);
        sAddress.setCity(city);
        this.sAddressRepository.save(sAddress);
        return sAddress;

    }

    public void modify(SAddress sAddress) {
        this.sAddressRepository.save(sAddress);
    }

    public void delete(SAddress sAddress) {
        this.sAddressRepository.delete(sAddress);
    }
}
