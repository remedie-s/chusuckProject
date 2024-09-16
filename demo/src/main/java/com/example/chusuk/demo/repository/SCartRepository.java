package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.SCart;

public interface SCartRepository extends JpaRepository<SCart, Integer> {

    List<SCart> findBySUserId(Integer userid);

}
