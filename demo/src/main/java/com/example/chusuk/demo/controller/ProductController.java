package com.example.chusuk.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chusuk.demo.dto.PReviewForm;
import com.example.chusuk.demo.dto.ProductForm;
import com.example.chusuk.demo.dto.SCartForm;
import com.example.chusuk.demo.entity.PReview;
import com.example.chusuk.demo.entity.PUser;
import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.service.PReviewService;
import com.example.chusuk.demo.service.ProductService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final SUserService sUserService;
    private final PReviewService pReviewService;

    @GetMapping("/list")
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model) {

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<Product> productPage = this.productService.getAllProductPage(pageable);

        List<Product> productsList = productPage.getContent();

        model.addAttribute("productsList", productsList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("totalItems", productPage.getTotalElements());

        return "product_list";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Integer id, ProductForm productForm, SCartForm sCartForm,
            PReviewForm pReviewForm, Model model) {

        Product product = this.productService.findById(id);
        log.info("제품세팅");
        List<PReview> reviewList = this.pReviewService.findByProductId(id);
        log.info("리뷰세팅");
        model.addAttribute("sCartForm", new SCartForm());
        model.addAttribute("product", product);
        model.addAttribute("reviewlist", reviewList);
        log.info("세팅완료");
        return "product_detail";

    }

    @GetMapping("/create")
    public String create(ProductForm productform, Principal principal, Model model) {
        String name = principal.getName();
        model.addAttribute("name", name);
        if (name.equals("seller") || name.equals("admin")) {
            return "product_form";
        } else {
            System.out.println("권한이 없는 사용자입니다.");
            return "index";
        }
    }

    @PostMapping("/create")
    public String create(@Valid ProductForm productForm, BindingResult bindingResult, HttpServletResponse response) {
        System.out.println("물품등록");
        if (bindingResult.hasErrors()) {
            bindingResult.getErrorCount();
            System.out.println("에러발생");
            return "product_form";
        }
        Product product;

        product = this.productService.create(productForm.getProductName(), productForm.getDescription(),
                productForm.getPrice(), productForm.getQuantity(), productForm.getCategory(),
                productForm.getImageUrl());
        System.out.println("물품등록 완료");
        this.pReviewService.create("바르고 고운말로 작성해주세요", product, this.sUserService.findById(1));
        return "redirect:/product/list";
        // 상품 디테일 페이지로 가게 해야되나?
        // TODO 물건 카테고리 추가요망
    }

    @GetMapping("/modify/{id}")
    public String modify(Model model, ProductForm productForm, @PathVariable("id") Integer id) {
        Product product = this.productService.findById(id);
        productForm.setProductName(product.getProductName());
        productForm.setDescription(product.getDescription());
        productForm.setPrice(product.getPrice());
        productForm.setQuantity(product.getQuantity());
        productForm.setCategory(product.getCategory());
        productForm.setImageUrl(product.getImageUrl());
        model.addAttribute("method", "put");

        return "product_form";
    }

    @PutMapping("/modify/{id}")
    public String modify(@Valid ProductForm productForm, BindingResult bindingResult,
            @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "prodcut_form";
        }
        Product product = this.productService.findById(id);

        product.setProductName(productForm.getProductName());
        product.setDescription(productForm.getDescription());
        product.setPrice(productForm.getPrice());
        product.setQuantity(productForm.getQuantity());
        product.setCategory(productForm.getCategory());
        product.setImageUrl(productForm.getImageUrl());

        this.productService.modify(product);

        return "redirect:/product/detail/" + id;
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        if (principal.getName().equals("admin") || principal.getName().equals("seller")) {
            Product product = this.productService.findById(id);
            this.productService.delete(product);
            System.out.println("삭제 성공");
            return "redirect:/product/list";
        }
        System.out.println("권한없는 사용자입니다");
        return "redirect:/product/list";
    }

    @GetMapping("/review/create/{id}")
    public String reviewCreate(@PathVariable("id") Integer id, Principal principal, Model model) {
        Product product = this.productService.findById(id);
        PReviewForm pReviewForm = new PReviewForm();
        pReviewForm.setProduct(product);
        model.addAttribute("product", product);
        model.addAttribute("reviewForm", pReviewForm);
        return "product_review_form";
    }

    @PostMapping("/review/create/{id}")
    public String reviewCreate(@PathVariable("id") Integer id, Model model, @Valid PReviewForm pReviewForm,
            BindingResult bindingResult,
            Principal principal) {

        if (bindingResult.hasErrors()) {
            System.out.println("리뷰 쓰기 중 에러가 있어요!");
            return "product_list";
        }
        String name = principal.getName();
        Integer userid = this.sUserService.findByUsername(name).getId();
        Product product = pReviewForm.getProduct();
        List<PUser> costomerList = product.getPUserList();
        System.out.println(costomerList);
        for (PUser pUser : costomerList) {
            if (userid == pUser.getId()) {
                this.pReviewService.create(pReviewForm.getContent(), product, this.sUserService.findByUsername(name));
                System.out.println("리뷰 등록 완료");
                return "redirect:/product/detail/" + id;
            }
        }
        System.out.println("리뷰쓰기 권한이 없어요!");
        return "product_list";
    }

    @GetMapping("/review/delete/{id}")
    public String reviewDelete(Model model, @PathVariable("id") Integer id, Principal principal) {
        String name = principal.getName();
        Integer userid = this.sUserService.findByUsername(name).getId();
        PReview review = this.pReviewService.selectOneReview(id);
        Integer productid = review.getProduct().getId();
        if (review.getSUser().getId() == userid || name.equals("admin") || name.equals("seller")) {
            this.pReviewService.delete(review);
            System.out.println("리뷰 삭제 완료");
        } else {
            System.out.println("리뷰쓰기 삭제 권한이 없어요!");
        }
        return "redirect:/product/detail/" + productid;
    }

}
