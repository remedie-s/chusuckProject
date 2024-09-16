package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.SOrder;

public interface SOrderRepository extends JpaRepository<SOrder, Integer> {

    List<SOrder> findBySUserId(Integer userid);

    Page<SOrder> findBySUserId(Integer userid, Pageable pageable);

}
