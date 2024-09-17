package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chusuk.demo.entity.SOrder;

public interface SOrderRepository extends JpaRepository<SOrder, Integer> {
    @Query("SELECT o FROM SOrder o WHERE o.sUser.id = :sUserId")
    List<SOrder> findBySUser_Id(@Param("sUserId") Integer sUserId);

    @Query("SELECT o FROM SOrder o WHERE o.sUser.id = :sUserId")
    Page<SOrder> findBySUser_Id(@Param("sUserId") Integer sUserId, Pageable pageable);

}
