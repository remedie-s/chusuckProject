package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.SUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PUserService {
    private final SUserRepository sUserRepository;
    private final PasswordEncoder passwordEncoder;

    public SUser create(String username, String password, String firstName, String lastName,
            String phoneNumber, String eMail) {
        SUser user = new SUser();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEMail(eMail);
        user.setUserGrade(0);
        user.setCreateDate(LocalDateTime.now());
        try {
            this.sUserRepository.save(user);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public SUser findbyId(Integer userId) {
        Optional<SUser> user = this.sUserRepository.findById(userId);
        if (user.isPresent()) {
            return user.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public SUser findbyUsername(String name) {
        Optional<SUser> user = this.sUserRepository.findByUsername(name);
        if (user.isPresent()) {
            return user.get();
        }
        throw new DataNotFoundException("user not found");
    }
}
