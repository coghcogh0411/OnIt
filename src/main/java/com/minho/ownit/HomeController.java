package com.minho.ownit;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;



@Controller
public class HomeController {
	@GetMapping("/")
	public String home(HttpServletRequest req) {
		req.setAttribute("contentPage", "home");
		return "index";
	}
	
	
	
}
