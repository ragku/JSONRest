package com.ragku.rest.web;

import com.ragku.rest.annotation.Controller;
import com.ragku.rest.annotation.GetMapping;

@Controller("")
public class HelloController {

	@GetMapping("/hello")
	public String hello(String word) {
		return "Hello:" + word;
	}
}
