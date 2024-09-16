package com.example.chusuk.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.SCart;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.SCartRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SCartService {
    private final SCartRepository sCartRepository;

    public SCart findbyId(Integer cartId) {
        Optional<SCart> scart = this.sCartRepository.findById(cartId);
        if (scart.isPresent()) {
            return scart.get();
        }
        throw new DataNotFoundException("user not found");
    }

    public void delete(SCart cart) {
        this.sCartRepository.delete(cart);
    }

    public void save(SCart sCart) {
        System.out.println("11111" + sCart);

        this.sCartRepository.save(sCart);
    }

    public SCart selectOneCart(Integer id) {
        Optional<SCart> sCart = this.sCartRepository.findById(id);
        if (sCart.isPresent()) {
            return sCart.get();
        }
        throw new DataNotFoundException("cart not found");
    }

    public List<SCart> findBySuserId(Integer userid) {

        List<SCart> carts = this.sCartRepository.findBySUserId(userid);
        if (carts.isEmpty()) {
            throw new DataNotFoundException("cart 가 없어요");
        }
        return carts;
    }

    @Transactional
    public void deleteByUserId(Integer userid) {
        List<SCart> carts = this.sCartRepository.findBySUserId(userid);
        if (carts.isEmpty()) {
            throw new DataNotFoundException("cart 가 없어요");
        }
        carts.clear();
    }
}
