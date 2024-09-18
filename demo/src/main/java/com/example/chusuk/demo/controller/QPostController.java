package com.example.chusuk.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.dto.QPostForm;
import com.example.chusuk.demo.dto.QReviewForm;
import com.example.chusuk.demo.entity.QPost;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.QPostService;
import com.example.chusuk.demo.service.QReviewService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/qpost")
public class QPostController {
    private final QPostService qPostService;
    private final QReviewService qReviewService;
    private final SUserService sUserService;

    @GetMapping("/list")
    public String list(Model model) {
        List<QPost> postlist = this.qPostService.findAll();
        model.addAttribute("postlist", postlist);
        return "qna_post_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") Integer id,
            QReviewForm reviewForm, Principal principal) {
        QPost post = this.qPostService.findById(id);
        String username = principal.getName();
        model.addAttribute("post", post);
        model.addAttribute("username", username);
        return "qna_post_detail";
    }

    @GetMapping("/create")
    public String create(Model model, QPostForm qnAPostFormostForm, Principal principal) {
        SUser user = this.sUserService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        return "qna_post_form";
    }

    @PostMapping("/create")
    public String create(@Valid QPostForm qPostForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println("에러가 있어요");
            return "qna_post_form";
        }
        SUser sUser = this.sUserService.findByUsername(principal.getName());

        this.qPostService.create(qPostForm.getSubject(), qPostForm.getContent(), sUser);
        System.out.println("저장합니다");
        return "redirect:/qpost/list";
    }

    @GetMapping("/modify/{id}")
    public String modify(Model model, QPostForm qPostForm, @PathVariable("id") Integer id) {
        QPost qPost = this.qPostService.findById(id);
        qPostForm.setSubject(qPost.getSubject());
        qPostForm.setContent(qPost.getContent());
        model.addAttribute("method", "put");
        model.addAttribute("qPostForm", qPostForm);
        return "qna_post_form";
    }

    @PutMapping("/modify/{id}")
    public String modify(@Valid QPostForm qPostForm, BindingResult bindingResult,
            @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "qna_post_form";
        }
        QPost qPost = this.qPostService.findById(id);
        qPost.setSubject(qPostForm.getSubject());
        qPost.setContent(qPostForm.getContent());
        this.qPostService.modify(qPost);
        return "redirect:/qpost/detail/" + id;
    }

    @DeleteMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id) {
        QPost qPost = this.qPostService.findById(id);
        this.qPostService.delete(qPost);
        return "redirect:/qpost/list";
    }

}
