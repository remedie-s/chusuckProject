package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.PUser;
import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.PUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PUserService {
    private final PUserRepository pUserRepository;

    public PUser create(SUser sUser, Product product, LocalDateTime localDateTime) {
        PUser user = new PUser();
        user.setSUser(sUser);
        user.setProduct(product);
        user.setCreateDate(LocalDateTime.now());
        try {
            this.pUserRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public PUser findbyId(Integer userId) {
        Optional<PUser> user = this.pUserRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public void deleteByPUser(PUser pUser) {
        this.pUserRepository.delete(pUser);
    }

    public PUser findBySUser_Id(Integer userId) {
        PUser pUser = this.pUserRepository.findBySUser_Id(userId);
        if (pUser == null) {
            throw new DataNotFoundException("구매리스트에 없어요");
        }
        return pUser;
    }

}
