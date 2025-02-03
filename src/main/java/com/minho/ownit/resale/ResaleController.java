package com.minho.ownit.resale;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ResaleController {
	@GetMapping("/resale")
	public String resaleHome(HttpServletRequest req) {
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	@GetMapping("/resale-reg")
	public String ResaleReg(HttpServletRequest req) {		
		req.setAttribute("contentPage", "resale/resalereg");
		return "index";
	}

	@GetMapping("/resale-product")
	public String ResaleProduct(HttpServletRequest req) {		
		req.setAttribute("contentPage", "resale/resaleproduct");
		return "index";
	}
	
}
