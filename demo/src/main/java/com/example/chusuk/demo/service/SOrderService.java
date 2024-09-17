package com.example.chusuk.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.SOrder;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.SOrderRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SOrderService {
    private final SOrderRepository sOrderRepository;

    public Page<SOrder> getAllOrder(Pageable pageable) {
        return this.sOrderRepository.findAll(pageable);
    }

    public Page<SOrder> findBySUserId(Integer userid, Pageable pageable) {
        Page<SOrder> orders = this.sOrderRepository.findBySUser_Id(userid, pageable);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("order 가 없어요");
        }
        return orders;
    }

    public List<SOrder> getAllOrder() {
        List<SOrder> orderlist = this.sOrderRepository.findAll();
        return orderlist;
    }

    public SOrder getOneOrder(Integer id) {
        Optional<SOrder> sorder = this.sOrderRepository.findById(id);
        if (sorder.isPresent()) {
            return sorder.get();
        }
        throw new DataNotFoundException("order not found");
    }

    public void delete(Integer id) {
        this.sOrderRepository.deleteById(id);
    }

    public void save(SOrder sorder) {
        this.sOrderRepository.save(sorder);
    }

    public List<SOrder> findBySUserId(Integer userid) {
        List<SOrder> orders = this.sOrderRepository.findBySUser_Id(userid);
        if (orders.isEmpty()) {
            throw new DataNotFoundException("order 가 없어요");
        }
        return orders;

    }

    public void delete(SOrder order) {
        this.sOrderRepository.delete(order);
    }

}
