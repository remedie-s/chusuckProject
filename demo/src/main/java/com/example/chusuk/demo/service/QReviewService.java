package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.QPost;
import com.example.chusuk.demo.entity.QReview;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.QReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QReviewService {
    private final QReviewRepository qReviewRepository;

    public QReview create(QPost post, String content, SUser sUser) {
        QReview qReview = new QReview();
        qReview.setContent(content);
        qReview.setCreateDate(LocalDateTime.now());
        qReview.setQPost(post);
        qReview.setSUser(sUser);
        this.qReviewRepository.save(qReview);
        return qReview;
    }

    public QReview selectOneReview(Integer id) {
        Optional<QReview> byId = this.qReviewRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new DataNotFoundException("리뷰가 없어요");
    }

    public void delete(QReview review) {
        this.qReviewRepository.delete(review);
    }

    public void modify(QReview review) {
        this.qReviewRepository.save(review);
    }

}
