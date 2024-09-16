package com.example.chusuk.demo.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.dto.SAddressForm;
import com.example.chusuk.demo.entity.SAddress;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.SAddressService;
import com.example.chusuk.demo.service.SUserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/address")
public class SAddressController {

    private final SAddressService sAddressService;
    private final SUserService sUserService;

    @GetMapping("/list")
    public String addressList(Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer id = user.getId();
        return "redirect:/address/list/" + id;
    }

    @GetMapping("/list/{id}")
    public String addressList(Model model, @PathVariable("id") Integer id, Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        List<SAddress> addresslist = user.getAddressList();
        model.addAttribute("addresslist", addresslist);
        return "address_list";
    }

    @GetMapping("/detail/{id}")
    public String addressdetail(Model model, @PathVariable("id") Integer id, SAddressForm sAddressForm) {
        SAddress address = this.sAddressService.getoneAddress(id);
        // model.addAttribute("address", address);
        return "address_detail" + id;
    }

    @GetMapping("/create")
    public String AddressCreate(SAddressForm sAddressForm, Principal principal, Model model) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        Integer id = user.getId();
        model.addAttribute("sAddressForm", new SAddressForm());
        return "addressform";
    }

    @PostMapping("/create")
    public String AddressCreate(@Valid SAddressForm sAddressForm, BindingResult bindingResult,
            Model model, Principal principal) {
        String name = principal.getName();
        System.out.println("1");
        SUser user = sUserService.findByUsername(name);
        Integer id = user.getId();
        // 같은지 검증할까?
        SAddress address;
        if (bindingResult.hasErrors()) {
            model.addAttribute("user", user);
            return "address_list";
        }
        System.out.println("2");
        address = this.sAddressService.create(user, sAddressForm.getStreetName(), sAddressForm.getBuildingNumber(),
                sAddressForm.getDetailAddress(), sAddressForm.getCity());
        System.out.println("3");
        return "redirect:/address/list";
    }

    @GetMapping("/modify/{id}")
    public String AddressModify(Model model, SAddressForm sAddressForm, @PathVariable("id") Integer id,
            Principal principal) {
        String name = principal.getName();
        SUser user = sUserService.findByUsername(name);
        id = user.getId();
        SAddress sAddress = this.sAddressService.getoneAddress(id);
        sAddressForm.setBuildingNumber(sAddress.getBuildingNumber());
        sAddressForm.setStreetName(sAddress.getStreetName());
        sAddressForm.setDetailAddress(sAddress.getDetailAddress());
        sAddressForm.setCity(sAddress.getCity());
        model.addAttribute("method", "put");
        return "address_form";
    }

    @PutMapping("/modify/{id}")
    public String AddressModify(@Valid SAddressForm sAddressForm, BindingResult bindingResult,
            @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "address_form";
        }
        SAddress sAddress = this.sAddressService.getoneAddress(id);
        sAddress.setBuildingNumber(sAddressForm.getBuildingNumber());
        sAddress.setStreetName(sAddressForm.getStreetName());
        sAddress.setDetailAddress(sAddressForm.getDetailAddress());
        sAddress.setCity(sAddressForm.getCity());
        this.sAddressService.modify(sAddress);
        return "redirect:/address/list";
    }

    @GetMapping("/delete/{id}")
    public String AddressDelete(@PathVariable("id") Integer id) {
        SAddress SAddress = this.sAddressService.getoneAddress(id);
        this.sAddressService.delete(SAddress);
        return "redirect:/address/list";
    }

}
