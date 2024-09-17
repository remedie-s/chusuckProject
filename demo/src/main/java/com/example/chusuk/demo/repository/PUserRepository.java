package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chusuk.demo.entity.PUser;

public interface PUserRepository extends JpaRepository<PUser, Integer> {
    @Query("SELECT o FROM PUser o WHERE o.sUser.id = :sUserId")
    PUser findBySUser_Id(@Param("sUserId") Integer userId);

}
