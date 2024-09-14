package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.QPost;

public interface QPostRepository extends JpaRepository<QPost, Long> {

}
