package com.example.chusuk.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.dto.CartListDto;
import com.example.chusuk.demo.dto.SCartForm;
import com.example.chusuk.demo.entity.SCart;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.ProductService;
import com.example.chusuk.demo.service.SCartService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class SCartController {
    private final SCartService sCartService;
    private final SUserService sUserService;
    private final ProductService productService;

    @GetMapping("/list")
    public String list(Model model, Principal principal) {
        // PathVariable 있어야할까?
        String name = principal.getName();
        SUser user = this.sUserService.findByUsername(name);
        Integer id = user.getId();
        // 키값하고 밸류값 따로 ArrayList로 보내는게 나을까?

        return "redirect:/cart/list/" + id;
    }

    @GetMapping("/list/{id}")
    public String list(@PathVariable("id") Integer id, Model model,
            Principal principal) {
        String name = principal.getName();
        SUser user = this.sUserService.findByUsername(name);
        Integer userid = user.getId();
        List<SCart> scart;
        try {
            scart = this.sCartService.findBySuserId(userid);
            if (scart == null) {
                scart = new ArrayList<>();
            }
        } catch (Exception e) {
            scart = new ArrayList<>();
            e.printStackTrace();
            return "index";
        }
        ArrayList<CartListDto> cartlist = new ArrayList<>();
        long cartsum = 0;
        for (SCart sCart : scart) {
            CartListDto cartListDto = new CartListDto();
            cartListDto.setId(sCart.getProduct().getId());
            cartListDto.setImageUrl(sCart.getProduct().getImageUrl());
            cartListDto.setProductName(sCart.getProduct().getProductName());
            cartListDto.setPrice(sCart.getProduct().getPrice());
            cartListDto.setQuantity(sCart.getQuantity());
            cartListDto.setSubtotal((sCart.getProduct().getPrice()) * (sCart.getQuantity()));
            cartlist.add(cartListDto);
            Integer quantity = sCart.getQuantity();
            Long price = sCart.getProduct().getPrice();
            cartsum += quantity * price;
        }

        model.addAttribute("cartsum", cartsum);
        model.addAttribute("cartlist", cartlist);
        return "cart_list";
    }

    @PostMapping("/add")
    public String addCart(
            @Valid SCartForm sCartForm, BindingResult bindingResult,
            Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "product_list";
        }
        String name = principal.getName();
        SUser user = this.sUserService.findByUsername(name);
        Integer userid = user.getId();

        List<SCart> scart;

        try {
            scart = this.sCartService.findBySuserId(userid);
            if (scart == null) {
                scart = new ArrayList<>();
            }
        } catch (Exception e) {
            scart = new ArrayList<>();
            e.printStackTrace();
        }

        Integer quantity = sCartForm.getQuantity();
        Integer productid = sCartForm.getProductid();
        // 브라우저에서 정상적으로 넘오는지 확인
        System.out.println(productid);
        System.out.println(quantity);
        if (scart.isEmpty()) {
            System.out.println("카트값 널인지 확인");
            SCart cart = new SCart();
            cart.setSUser(user);
            cart.setProduct(this.productService.findById(productid));
            cart.setQuantity(quantity);
            cart.setCreateDate(LocalDateTime.now());
            this.sCartService.save(cart);
            return "redirect:/cart/list/" + userid;
        }
        System.out.println("카트리스트 카트리스트에 물건 양 등록");
        System.out.println("프로덕트 값이 있나 확인");
        boolean productIsPresent = false;
        if (!scart.isEmpty()) {
            for (SCart sCarts : scart) {
                if (sCarts.getProduct().getId().equals(productid)) {
                    // 프러덕트 아이디를 받아와서 확인
                    sCarts.setQuantity(sCarts.getQuantity() + quantity);
                    productIsPresent = true;
                    System.out.println("물건이 있음");
                    this.sCartService.save(sCarts);
                    return "redirect:/cart/list/" + userid;
                }
            }
        }
        if (!productIsPresent) {
            System.out.println("물건이 없음");
            SCart scart1 = new SCart();
            scart1.setSUser(user);
            scart1.setProduct(this.productService.findById(productid));
            scart1.setQuantity(quantity);
            scart1.setCreateDate(LocalDateTime.now());
            System.out.println("물건이 없을때 넣는값" + scart1);
            this.sCartService.save(scart1);
        }

        return "redirect:/cart/list/" + userid;
    }

    @GetMapping("/delete/{id}")
    public String deleteCart(@PathVariable("id") Integer id, Principal principal) {
        SCart sCart = this.sCartService.selectOneCart(id);
        this.sCartService.delete(sCart);
        System.out.println("카트 물품 삭제 성공");

        return "redirect:/cart/list";
    }

    @RequestMapping("/sum")
    public void sum(Principal principal, Model model) {
        String name = principal.getName();
        SUser user = this.sUserService.findByUsername(name);
        Integer userid = user.getId();
        List<SCart> cartlist = this.sCartService.findBySuserId(userid);
        Long sum = 0L;

        if (cartlist != null) {
            for (SCart scart : cartlist) {
                Integer quantity = scart.getQuantity();
                Long price = scart.getProduct().getPrice();
                // 프로덕트 아이디를 받아와서 프로덕트를 가져온다음 거기서 가격을 뽑아야함
                sum += quantity * price;
            }
        }
        model.addAttribute("cartSum", sum);

    }

}
