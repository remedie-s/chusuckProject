package com.example.chusuk.demo.controller;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.chusuk.demo.dto.OrderListDto;
import com.example.chusuk.demo.dto.SCartForm;
import com.example.chusuk.demo.dto.SOrderForm;
import com.example.chusuk.demo.entity.PUser;
import com.example.chusuk.demo.entity.Product;
import com.example.chusuk.demo.entity.SCart;
import com.example.chusuk.demo.entity.SOrder;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.PUserService;
import com.example.chusuk.demo.service.ProductService;
import com.example.chusuk.demo.service.SCartService;
import com.example.chusuk.demo.service.SOrderService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class SOrderController {

    private final SOrderService sOrderService;
    private final SCartService sCartService;
    private final SUserService sUserService;
    private final ProductService productService;
    private final PUserService pUserService;

    @GetMapping("/list")
    public String orderList(Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer id = user.getId();
        return "redirect:/order/list/" + id;
    }

    @GetMapping("/list/{id}")
    public String orderList(@PathVariable("id") Integer id, @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model, Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer userId = user.getId();
        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<SOrder> sOrderPage = this.sOrderService.findBySUserId(userId, pageable);
        ArrayList<OrderListDto> orderList = new ArrayList<>();
        Long orderSum = 0L;
        for (SOrder sOrder : sOrderPage.getContent()) {
            OrderListDto orderListDto = new OrderListDto();
            orderListDto.setProduct(sOrder.getProduct());
            orderListDto.setSOrder(sOrder);
            orderListDto.setSUser(sOrder.getSUser());
            orderListDto.setSubtotal(sOrder.getQuantity() * sOrder.getProduct().getPrice());
            orderList.add(orderListDto);
            Integer quantity = sOrder.getQuantity();
            Long price = sOrder.getProduct().getPrice();
            orderSum += quantity * price;
        }
        model.addAttribute("orderSum", orderSum);
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sOrderPage.getTotalPages());
        model.addAttribute("totalItems", sOrderPage.getTotalElements());

        return "order_list";
    }

    @GetMapping("/detail/{id}")
    public String orderDetail(Model model, @PathVariable("id") Integer id) {
        SOrder order = this.sOrderService.getOneOrder(id);
        model.addAttribute("order", order);
        return "order_detail";
    }

    @GetMapping("/create")
    public String create(SOrderForm sOrderForm, SCartForm sCartForm, Model model, Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer userId = user.getId();

        List<SCart> cartList = this.sUserService.findByUsername(name).getSCartList();

        // 카트 확인(비어있는지 확인)
        if (cartList.isEmpty()) {
            System.out.println("카트가 비어있어요");
            return "product_list";
        } else {
            System.out.println("카트가 들어있습니다.");
            return "order_form";
        }
    }

    @PostMapping("/create")
    public String create(@Valid SOrderForm spOrderForm, BindingResult bindingResult, SCartForm spCartForm,
            Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            System.out.println("에러가 있어요");
            return "product_list";
        }
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer userId = user.getId();
        List<SCart> cartList = this.sUserService.findByUsername(name).getSCartList();
        if (cartList.isEmpty()) {
            System.out.println("카트가 비어있어요");
            return "product_list";
        }
        System.out.println("카트가 들어있습니다.");
        // 카트에서 카트리스트 가져온 후 오더에 있는 리스트에 복사
        for (SCart sCart : cartList) {
            Product orderedProduct = sCart.getProduct();
            if (orderedProduct.getQuantity() >= sCart.getQuantity()) {
                SOrder sOrder = new SOrder();
                sOrder.setSUser(user);
                sOrder.setProduct(orderedProduct);
                sOrder.setQuantity(sCart.getQuantity());
                sOrder.setCreateDate(LocalDateTime.now());
                sOrder.setStatus(0);
                sOrder.setRequest(0);
                this.sOrderService.save(sOrder);
                // 주문 생성 시 주문한 물품 재고량에서 뺌
                orderedProduct.setQuantity(orderedProduct.getQuantity() - sCart.getQuantity());
                this.productService.save(orderedProduct); // 수정된 상품 재고를 저장합니다
                System.out.println("주문생성 완료1");
            } else {
                System.out.println("재고량보다 주문물품 수량이 많습니다.");
            }
        }
        System.out.println("주문생성 완료2");
        System.out.println("카트 삭제");
        this.sCartService.deleteByUserId(userId);

        return "redirect:/order/list/" + userId;
    }

    @GetMapping("/seller/list")
    public String orderSellerList(@RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size, Model model, Principal principal) {
        if (!principal.getName().equals("seller") && !principal.getName().equals("admin")) {
            return "order_list";
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<SOrder> sOrderPage = this.sOrderService.getAllOrder(pageable);

        ArrayList<OrderListDto> orderList = new ArrayList<>();
        Long orderSum = 0L;
        for (SOrder sOrder : sOrderPage.getContent()) {
            if (!sOrder.getStatus().equals(3)) {
                OrderListDto orderListDto = new OrderListDto();
                orderListDto.setProduct(sOrder.getProduct());
                System.out.println(sOrder.getProduct());
                orderListDto.setSOrder(sOrder);
                orderListDto.setSUser(sOrder.getSUser());
                orderListDto.setSubtotal(sOrder.getQuantity() * sOrder.getProduct().getPrice());
                orderList.add(orderListDto);
                Integer quantity = sOrder.getQuantity();
                Long price = sOrder.getProduct().getPrice();
                orderSum += quantity * price;
            }
        }

        model.addAttribute("orderSum", orderSum);
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sOrderPage.getTotalPages());
        model.addAttribute("totalItems", sOrderPage.getTotalElements());
        return "order_seller_list";
    }

    @GetMapping("/seller/list/complete")
    public String orderSellerListCom(Principal principal,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            Model model) {

        if (!principal.getName().equals("seller") && !principal.getName().equals("admin")) {
            return "order_list";
        }

        PageRequest pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createDate"));
        Page<SOrder> sOrderPage = this.sOrderService.getAllOrder(pageable);

        ArrayList<OrderListDto> orderList = new ArrayList<>();
        Long orderSum = 0L;
        for (SOrder sOrder : sOrderPage.getContent()) {
            if (sOrder.getStatus().equals(3)) {
                OrderListDto orderListDto = new OrderListDto();
                orderListDto.setProduct(sOrder.getProduct());
                orderListDto.setSOrder(sOrder);
                orderListDto.setSUser(sOrder.getSUser());
                orderListDto.setSubtotal(sOrder.getQuantity() * sOrder.getProduct().getPrice());
                orderList.add(orderListDto);
                Integer quantity = sOrder.getQuantity();
                Long price = sOrder.getProduct().getPrice();
                orderSum += quantity * price;
            }
        }

        model.addAttribute("orderSum", orderSum);
        model.addAttribute("orderList", orderList);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", sOrderPage.getTotalPages());
        model.addAttribute("totalItems", sOrderPage.getTotalElements());
        return "order_seller_list";
    }

    @GetMapping("/accept/{id}")
    public String accept(@PathVariable("id") Integer id, Principal principal) {
        // 아이디가 셀러 혹은 어드민일 경우에만 주문상태 변경가능

        if (principal.getName().equals("seller") || principal.getName().equals("admin")) {
            // 모델로 받는게 나을려나
            System.out.println("찾기 시작");
            SOrder order = this.sOrderService.getOneOrder(id);
            System.out.println("오더하나 찾기");
            order.setRequest(1);
            System.out.println("리퀘스트 상태 바꿈");
            order.setStatus(1);
            System.out.println("오더상태 바꿈");
            this.sOrderService.save(order);
            System.out.println("오더 저장");
            return "redirect:/order/seller/list";
        } else {
            System.out.println("권한이 없습니다");
            return "order_list";
        }
    }

    @GetMapping("/refund/{id}")
    public String refund(@PathVariable("id") Integer id, Principal principal) {
        // 아이디가 셀러 혹은 어드민일 경우에만 주문상태 변경가능
        System.out.println("찾기 시작");
        SOrder order = this.sOrderService.getOneOrder(id);
        System.out.println("오더하나 찾기");

        if (principal.getName().equals(this.sUserService.findById(id).getUsername())) {
            System.out.println("동일성 검사");

            order.setRequest(10);
            System.out.println("리퀘스트 상태 바꿈");
            order.setStatus(10);
            System.out.println("오더상태 바꿈");
            this.sOrderService.save(order);
            System.out.println("오더 저장");
            return "redirect:/order/seller/list";
        } else {
            System.out.println("권한이 없습니다");
            return "order_list";
        }
    }

    @GetMapping("/arrive/{id}")
    public String arrive(@PathVariable("id") Integer id, SOrderForm spOrderForm, Model model, Principal principal) {
        if (principal.getName().equals("seller") || principal.getName().equals("admin")) {
            SOrder order = this.sOrderService.getOneOrder(id);
            order.setStatus(2);
            this.sOrderService.save(order);
            return "redirect:/order/seller/list";
        } else {
            System.out.println("권한이 없습니다");
            return "order_list";

        }
    }

    @GetMapping("/end/{id}")
    public String end(@PathVariable("id") Integer id, SOrderForm spOrderForm, Model model, Principal principal) {
        if (principal.getName().equals("seller") || principal.getName().equals("admin")) {
            SOrder order = this.sOrderService.getOneOrder(id);
            order.setStatus(3);
            this.sOrderService.save(order);

            SUser user = order.getSUser();
            Product product = order.getProduct();
            this.pUserService.create(user, product, LocalDateTime.now());
            return "redirect:/order/seller/list";

        } else {
            System.out.println("권한이 없습니다");
            return "order_list";
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Integer id, Principal principal) {
        if (principal.getName().equals("seller") || principal.getName().equals("admin")) {
            SOrder order = this.sOrderService.getOneOrder(id);
            Integer userId = order.getSUser().getId();
            // 오더 수량만큼 재고 수량에 + 할까? 망실물도있을껀데 농산물은 반품안되긴할껀데
            Product product = order.getProduct();
            product.setQuantity(product.getQuantity() + order.getQuantity());
            this.productService.save(product);
            this.sOrderService.delete(order);
            System.out.println("반품 완료");
            PUser findbyId = this.pUserService.findbyId(userId);
            this.pUserService.deleteByPUser(findbyId);
            System.out.println("리뷰허용자목록 삭제 완료");

            return "redirect:/order/list";
        } else {
            System.out.println("권한이 없습니다");
            return "order_list";
        }

    }
}
