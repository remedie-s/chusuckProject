package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.SUser;

public interface SUserRepository extends JpaRepository<SUser, Integer> {

}
