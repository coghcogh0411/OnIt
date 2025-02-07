package com.minho.ownit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import com.minho.ownit.member.MemberDAO;



@Controller
public class HomeController {
	@Autowired
	private MemberDAO mDAO;
	
	@GetMapping("/")
	public String home(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return "index";
	}
}
