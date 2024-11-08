package com.example.chusuk.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.example.chusuk.demo.service.TokenProvider;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final TokenProvider tokenProvider;

        @Bean
        SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((a) -> a.requestMatchers(new AntPathRequestMatcher("/suser/**"))
                                                .permitAll())
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/homepage/**")).permitAll())
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/product/upload/**"))
                                                .hasAnyRole("SELLER", "ADMIN"))
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/qpost/**")).permitAll())
                                .authorizeHttpRequests(
                                                (a) -> a.requestMatchers(new AntPathRequestMatcher("/qpost/review/**"))
                                                                .hasAnyRole("SELLER", "ADMIN"))
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/product/**")).permitAll())
                                .authorizeHttpRequests(
                                                (a) -> a.requestMatchers(
                                                                new AntPathRequestMatcher("/product/detail/**"))
                                                                .permitAll())
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/sorder/**")).permitAll())
                                .authorizeHttpRequests((a) -> a.requestMatchers(new AntPathRequestMatcher("/dist/**"))
                                                .permitAll())
                                .authorizeHttpRequests((a) -> a
                                                .requestMatchers(new AntPathRequestMatcher("/plugins/**")).permitAll())
                                .authorizeHttpRequests((a) -> a.requestMatchers(new AntPathRequestMatcher("/api/**"))
                                                .permitAll())
                                .authorizeHttpRequests((a) -> a.requestMatchers(new AntPathRequestMatcher("/cart/**"))
                                                .permitAll())
                                .authorizeHttpRequests((a) -> a.requestMatchers(new AntPathRequestMatcher("/order/**"))
                                                .permitAll())
                                .authorizeHttpRequests(
                                                (a) -> a.requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                                // (new AntPathRequestMatcher("/**")).authenticated() )
                                // .csrf().disable()
                                .csrf((b) -> b.ignoringRequestMatchers(new AntPathRequestMatcher("/api/**")))
                                .csrf((b) -> b.ignoringRequestMatchers(new AntPathRequestMatcher("/dist/**")))
                                .csrf((b) -> b.ignoringRequestMatchers(new AntPathRequestMatcher("/plugins/**")))
                                .headers((c) -> c.addHeaderWriter(new XFrameOptionsHeaderWriter()))

                                .formLogin((formLogin) -> formLogin
                                                .loginPage("/suser/login")
                                                .defaultSuccessUrl("/index"))

                                .logout((logout) -> logout
                                                .logoutRequestMatcher(new AntPathRequestMatcher("/suser/logout"))
                                                .logoutSuccessUrl("/suser/login")
                                                .invalidateHttpSession(true))

                                .addFilterBefore(new TokenAuthenticationFilter(tokenProvider),
                                                UsernamePasswordAuthenticationFilter.class)

                ;
                return http.build();

        }

        @Bean
        PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

}
