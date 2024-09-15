package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.PReview;

public interface PReviewRepository extends JpaRepository<PReview, Integer> {

    List<PReview> findByProductId(Integer id);

}
