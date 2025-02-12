package com.minho.ownit.resale;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ResaleController {
	@Autowired
	private MemberDAO mDAO;
	
	@Autowired
	private ResaleDAO rDAO;
	
	@GetMapping("/resale")
	public String resaleHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	@GetMapping("/resale-go-reg")
	public String ResaleGoReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
		List<ResaleCategory> categories = rDAO.getAllCategories();
        req.setAttribute("categories", categories);
        req.setAttribute("contentPage", "resale/resalereg");
		return "index";
	}

	@GetMapping("/resale-product")
	public String ResaleProduct(HttpServletRequest req) {	
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "resale/resaleproduct");
		return "index";
	}
	
	@PostMapping("/resale-reg")
	public String ResaleReg(Resale resale, @RequestParam("files") MultipartFile[] files, HttpServletRequest req) {
		mDAO.isLogined(req);
		rDAO.resaleReg(resale, req, files);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	
}
