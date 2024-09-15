package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getAllProduct() {

        return this.productRepository.findAll();
    }

    public Product create(String productName, String description, Long price, Integer quantity,
            String imageUrl) {
        Product p = new Product();
        p.setProductName(productName);
        p.setDescription(description);
        p.setPrice(price);
        p.setQuantity(quantity);
        p.setImageUrl(imageUrl);
        p.setCreateDate(LocalDateTime.now());
        this.productRepository.save(p);
        return p;
    }

    public void delete(Product product) {
        this.productRepository.delete(product);
    }

    public void modify(Product product) {
        this.productRepository.save(product);
    }

    public void save(Product product) {
        this.productRepository.save(product);
    }

    public Product findById(Integer id) {
        Optional<Product> product = this.productRepository.findById(id);
        if (product.isPresent()) {
            return product.get();
        }
        throw new DataNotFoundException("product not found");
    }
}
