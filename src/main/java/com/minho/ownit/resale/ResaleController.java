package com.minho.ownit.resale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ResaleController {
	@Autowired
	private MemberDAO mDAO;
	
	@GetMapping("/resale")
	public String resaleHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	@GetMapping("/resale-reg")
	public String ResaleReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "resale/resalereg");
		return "index";
	}

	@GetMapping("/resale-product")
	public String ResaleProduct(HttpServletRequest req) {	
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "resale/resaleproduct");
		return "index";
	}
	
}
