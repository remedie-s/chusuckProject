package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.PUser;

public interface PUserRepository extends JpaRepository<PUser, Integer> {

}
