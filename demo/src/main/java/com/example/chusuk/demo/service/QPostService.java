package com.example.chusuk.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.chusuk.demo.entity.QPost;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.repository.QPostRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QPostService {

    private final QPostRepository qPostRepository;

    public Page<QPost> getAllOrder(Pageable pageable) {
        return this.qPostRepository.findAll(pageable);
    }

    public Page<QPost> findBySUser_Id(Integer userid, Pageable pageable) {
        Page<QPost> qpostList = this.qPostRepository.findAllBySUser_Id(userid, pageable);
        if (qpostList.isEmpty()) {

            throw new DataNotFoundException("QnA Post 가 없어요");

        }
        return qpostList;

    }

    public QPost findById(Integer id) {
        Optional<QPost> byId = this.qPostRepository.findById(id);
        if (byId.isPresent()) {
            return byId.get();
        }
        throw new DataNotFoundException("Q&A POST 가 없어요");
    }

    public List<QPost> findAll() {
        return this.qPostRepository.findAll();
    }

    public QPost create(String subject, String content, SUser sUser) {
        QPost qPost = new QPost();
        qPost.setContent(content);
        qPost.setSubject(subject);
        qPost.setSUser(sUser);
        qPost.setCreateDate(LocalDateTime.now());
        qPost.setQnaStatus(0);
        this.qPostRepository.save(qPost);
        return qPost;
    }

    public void delete(QPost qPost) {
        this.qPostRepository.delete(qPost);
    }

    public void modify(QPost qPost) {
        this.qPostRepository.save(qPost);
    }

    public List<QPost> findBySUser_Id(Integer userid) {
        List<QPost> qpostList = this.qPostRepository.findAllBySUser_Id(userid);
        return qpostList;

    }

}
