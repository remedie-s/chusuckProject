package com.example.chusuk.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.chusuk.demo.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

}
