package com.minho.ownit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.RequestParam;

import com.minho.ownit.community.CommunityDAO;
import com.minho.ownit.member.MemberDAO;



@Controller
public class HomeController {
	@Autowired
	private MemberDAO mDAO;
	@Autowired
	private CommunityDAO cDAO;
	
	@GetMapping("/")
	public String home(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return "index";
	}
	@GetMapping("/search")
	public String Search(@RequestParam String searchType, @RequestParam String keyword, HttpServletRequest req) {

		switch (searchType) {
		case "community":
			mDAO.isLogined(req);
			cDAO.search(req);
			cDAO.get(req, 1);
			req.setAttribute("contentPage", "community/communityhome");
			break;

		default:
			break;
		}
		return "index";
	}
	
}
