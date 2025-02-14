package com.minho.ownit.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuctionController {
	@Autowired
	private MemberDAO mDAO;
	
	@Autowired
	private AuctionDAO aDAO;
	
	@GetMapping("/auction")
	public String resaleHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAllResaleItems(req);
		req.setAttribute("contentPage", "auction/auctionhome");
		return "index";
	}
	@GetMapping("/auction-go-reg")
	public String AuctionGoReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
        req.setAttribute("contentPage", "auction/auctionreg");
		return "index";
	}
}
