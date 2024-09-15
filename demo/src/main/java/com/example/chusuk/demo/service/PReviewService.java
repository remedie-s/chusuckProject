package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.PReview;
import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.PReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PReviewService {
    private final PReviewRepository pReviewRepository;

    public List<PReview> findByProductId(Integer id) {
        List<PReview> reviewList = this.pReviewRepository.findByProductId(id);
        if (!reviewList.isEmpty()) {
            return reviewList;
        }
        throw new DataNotFoundException("review not found");
    }

    public PReview create(String content, Product product, SUser sUser) {
        PReview pReview = new PReview();
        pReview.setContent(content);
        pReview.setCreateTime(LocalDateTime.now());
        pReview.setProduct(product);
        pReview.setSUser(sUser);
        try {
            this.pReviewRepository.save(pReview);
        } catch (Exception e) {
            System.out.println("리뷰 작성중 문제가 있습니다");
            e.printStackTrace();
        }
        return pReview;
    }

    public PReview selectOneReview(Integer id) {
        Optional<PReview> byId = this.pReviewRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new DataNotFoundException("review not found");

    }

    public void delete(PReview review) {

        this.pReviewRepository.deleteById(review.getId());
        System.out.println("리뷰 삭제완료");
    }

}
