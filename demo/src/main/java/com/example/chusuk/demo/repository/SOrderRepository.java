package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.SOrder;

public interface SOrderRepository extends JpaRepository<SOrder, Integer> {

}
