package com.example.chusuk.demo.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.chusuk.demo.entity.QPost;

public interface QPostRepository extends JpaRepository<QPost, Integer> {
    @Query("SELECT q From QPost q WHERE q.sUser.id = :sUserId")
    List<QPost> findAllBySUser_Id(@Param("sUserId") Integer userid);

    @Query("SELECT q From QPost q WHERE q.sUser.id = :sUserId")
    Page<QPost> findAllBySUser_Id(@Param("sUserId") Integer userid, Pageable pageable);
}
