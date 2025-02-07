package com.minho.ownit.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CommunityController {
	@Autowired
	private MemberDAO mDAO;
	
	@GetMapping("/community")
	public String CommunityHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "community/communityhome");
		return "index";
	}
	@GetMapping("/community-reg")
	public String CommunityReg(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "community/communityreg");
		return "index";
	}
	
}
