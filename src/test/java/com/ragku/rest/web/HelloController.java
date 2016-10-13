package com.ragku.rest.web;

import com.ragku.rest.GetMapping;

public class HelloController {

	@GetMapping("/hello")
	public String hello(String word) {
		return "Hello:" + word;
	}
}
