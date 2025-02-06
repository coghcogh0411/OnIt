package com.minho.ownit.community;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CommunityController {
	@GetMapping("/community")
	public String CommunityHome(HttpServletRequest req) {
		req.setAttribute("contentPage", "community/communityhome");
		return "index";
	}
	@GetMapping("/community-reg")
	public String CommunityReg(HttpServletRequest req) {
		req.setAttribute("contentPage", "community/communityreg");
		return "index";
	}
	
}
