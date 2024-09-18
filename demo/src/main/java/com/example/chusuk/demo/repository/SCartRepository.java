package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chusuk.demo.entity.SCart;

public interface SCartRepository extends JpaRepository<SCart, Integer> {
    @Query("SELECT c FROM SCart c WHERE c.sUser.id = :sUserId")
    List<SCart> findBySUser_Id(@Param("sUserId") Integer sUserId);

    @Query("SELECT c FROM SCart c WHERE c.sUser.id = :sUserId")
    Page<SCart> findBySUser_Id(@Param("sUserId") Integer sUserId, Pageable pageable);

}
