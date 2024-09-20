package com.example.chusuk.demo.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.dto.QReviewForm;
import com.example.chusuk.demo.entity.QPost;
import com.example.chusuk.demo.entity.QReview;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.QPostService;
import com.example.chusuk.demo.service.QReviewService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qpost/review")
public class QReviewController {
    private final QPostService qPostService;
    private final QReviewService qReviewService;
    private final SUserService sUserService;

    @GetMapping("/create/{id}")
    public String create(Model model, @PathVariable("id") Integer id,
            @Valid QReviewForm reviewForm, BindingResult bindingResult, Principal principal) {
        String username = principal.getName();
        SUser sUser = this.sUserService.findByUsername(username);

        QPost post = this.qPostService.findById(id);
        if (bindingResult.hasErrors()) {
            model.addAttribute("post", post);
            return "qna_detail";
        }
        this.qReviewService.create(post, reviewForm.getContent(), sUser);
        return "redirect:/qnapost/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        // 리뷰 ID => 리뷰 엔티티 획득
        QReview review = this.qReviewService.selectOneReview(id);
        this.qReviewService.delete(review);
        // Post ID를 획득해서 해당 Post의 상세페이지로 이동
        return "redirect:/qpost/detail/" + review.getQPost().getId();
    }

    @GetMapping("/modify/{id}")
    public String modify(QReviewForm reviewForm, @PathVariable("id") Integer id) {
        // 실습 1분
        // Review 내용 획득
        QReview review = this.qReviewService.selectOneReview(id);
        // reviewForm에 내용 설정
        reviewForm.setContent(review.getContent());
        return "qna_review_form";
    }

    @PostMapping("/modify/{id}")
    public String modify(@Valid QReviewForm reviewForm, BindingResult bindingResult,
            @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "qna_review_form";
        }
        QReview review = this.qReviewService.selectOneReview(id);
        review.setContent(reviewForm.getContent());
        this.qReviewService.modify(review);
        return "redirect:/qpost/detail/" + review.getQPost().getId();
    }
}
