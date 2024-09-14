package com.example.chusuk.demo.controller;

import java.time.Duration;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.chusuk.demo.config.JwtProperties;
import com.example.chusuk.demo.dto.SUserForm;
import com.example.chusuk.demo.entity.SUser;
import com.example.chusuk.demo.service.SUserService;
import com.example.chusuk.demo.service.TokenProvider;
import com.example.chusuk.demo.service.TokenService;
import com.example.chusuk.demo.service.UtilService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/suser")
public class SUserController {
    private final SUserService sUserService;
    private final TokenProvider tokenProvider;
    private final TokenService tokenService;
    private final JwtProperties jwtProperties;
    private final UtilService utilService;

    @GetMapping("/{id}")
    public String usermain(@PathVariable("id") Integer id, Model model) {
        this.sUserService.findById(id);
        return "user_main";
    }

    @GetMapping("/signup")
    public String signup(Model model) {
        model.addAttribute("sUserForm", new SUserForm());
        return "signup_form";
    }

    @PostMapping("/signup")
    public String signup(@Valid SUserForm sUserForm, BindingResult bindingResult, HttpServletResponse response) {
        System.out.println("가입요청");
        if (bindingResult.hasErrors()) {
            System.out.println("에러발생" + bindingResult.getAllErrors().size());
            for (ObjectError err : bindingResult.getAllErrors()) {
                log.info(err.getDefaultMessage());
            }
            return "signup_form";
        }
        System.out.println("비번체크");
        if (!sUserForm.getPassword1().equals(sUserForm.getPassword2())) {
            bindingResult.rejectValue("password", "passwordInCorrect", "비밀번호가 서로다릅니다.");
            System.out.println("비밀번호가 다릅니다.");
            return "signup_form";
        }
        SUser sUser;
        try {
            System.out.println("회원가입 진행");
            sUser = this.sUserService.create(sUserForm.getUsername(), sUserForm.getPassword1(),
                    sUserForm.getFirstName(),
                    sUserForm.getLastName(), sUserForm.getPhoneNumber(),
                    sUserForm.getEMail());
            System.out.println("회원가입 완료");
        } catch (DataIntegrityViolationException e) {
            bindingResult.reject("signupError", "이미 사용중인 회원정보입니다.");
            return "signup_form";
        } catch (Exception e) {
            bindingResult.reject("signupError", "회원가입 오류입니다.");
            return "signup_form";
        }
        String accessToken = this.tokenProvider.generateToken(sUser, Duration.ofDays(7));
        String refreshToken = this.tokenProvider.generateToken(sUser, Duration.ofDays(30));
        System.out.println(sUser.getId());
        this.tokenService.saveRefreshToken(sUser.getId(), refreshToken);
        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), response);
        utilService.setCookie("refresh_token", refreshToken, utilService.toSecondOfDay(30), response);

        return "redirect:/user/login";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest httpServletRequest) {
        httpServletRequest.getCookies();
        return "login_form";
    }

    @GetMapping("/reissue/{rToken}")
    public String reissue(@PathVariable("rToken") String rToken, HttpServletResponse httpServletResponse) {
        String accessToken = tokenService.createNewAccessToken(rToken, 24 * 7);
        utilService.setCookie("access_token", accessToken, utilService.toSecondOfDay(7), httpServletResponse);
        return "redirect:/";
    }
}
