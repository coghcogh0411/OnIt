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
	@GetMapping("/join")
	public String join(HttpServletRequest req) {
		req.setAttribute("contentPage", "member/join");
		return "index";
	}
	@GetMapping("/selectjoin")
	public String selectlogin(HttpServletRequest req) {
		req.setAttribute("contentPage", "member/selectjoin");
		return "index";
	}
	@GetMapping("/login")
	public String login(HttpServletRequest req) {
		req.setAttribute("contentPage", "member/login");
		return "index";
	}
	
}
