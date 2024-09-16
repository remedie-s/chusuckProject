package com.example.chusuk.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.example.chusuk.demo.repository.SCartRepository;

import lombok.RequiredArgsConstructor;

@SpringBootTest
@RequiredArgsConstructor
class DemoApplicationTests {
	private final SCartRepository sCartRepository;

	@Test
	void contextLoads() {

	}

}
