package com.example.chusuk.demo.controller;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chusuk.demo.dto.QPostForm;
import com.example.chusuk.demo.dto.QReviewForm;
import com.example.chusuk.demo.entity.QPost;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.exception.DataNotFoundException;
import com.example.chusuk.demo.service.QPostService;
import com.example.chusuk.demo.service.QReviewService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/qpost")
public class QPostController {
    private final QPostService qPostService;
    private final QReviewService qReviewService;
    private final SUserService sUserService;

    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        String username = principal.getName();
        SUser sUser = this.sUserService.findByUsername(username);
        Integer id = sUser.getId();
        // List<QPost> postlist = this.qPostService.findBySUser_Id(userid);
        // model.addAttribute("postlist", postlist);
        // return "qna_post_list";
        return "redirect:/qpost/list/" + id;
    }

    @GetMapping("/list/{id}")
    public String list(@PathVariable("id") Integer id, @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model, Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer userId = user.getId();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<QPost> qPostPage = this.qPostService.findBySUser_Id(userId, pageable);
        if (qPostPage.isEmpty()) {
            throw new DataNotFoundException("qna가 비었어요");
        }
        model.addAttribute("qPostPage", qPostPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", qPostPage.getTotalPages());
        model.addAttribute("totalItems", qPostPage.getTotalElements());
        return "qna_post_list";

    }

    @GetMapping("/seller/list")
    public String sellerList(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model, Principal principal) {
        String username = principal.getName();
        if (username.equals("seller") || username.equals("admin")) {
            PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
            Page<QPost> qPostPage = this.qPostService.getAllOrder(pageable);
            model.addAttribute("qPostPage", qPostPage);
            return "qna_post_list";
        } else {
            log.info("당신은 관리자이거나 셀러가 아닙니다.");
            return "index";
        }
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
    public String create(Model model, QPostForm qPostForm, Principal principal) {
        SUser user = this.sUserService.findByUsername(principal.getName());
        qPostForm = new QPostForm();
        String method = "post";
        model.addAttribute("user", user);
        model.addAttribute("qPostForm", qPostForm);
        model.addAttribute("method", method);

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
        // TODO 바꿔야함
    }

    @GetMapping("/modify/{id}")
    public String modify(Model model, QPostForm qPostForm, @PathVariable("id") Integer id) {
        QPost qPost = this.qPostService.findById(id);
        qPostForm.setSubject(qPost.getSubject());
        qPostForm.setContent(qPost.getContent());
        String method = "put";
        model.addAttribute("method", method);
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
