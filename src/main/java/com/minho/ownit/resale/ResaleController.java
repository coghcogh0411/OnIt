package com.minho.ownit.resale;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.member.MemberDAO;

import jakarta.annotation.Resource;
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
		rDAO.getAllCategories(req);
		rDAO.getAllResaleItems(req);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	@GetMapping("/resale-category")
	public String getResaleByCategory(@RequestParam("no") int pno, HttpServletRequest req) {
	    mDAO.isLogined(req);
	    rDAO.getResaleByCategory(req, pno);
	    req.setAttribute("contentPage", "resale/resalehome");
	    return "index";
	}
	
	@GetMapping("/resale-go-reg")
	public String ResaleGoReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
		rDAO.getAllCategories(req);
        req.setAttribute("contentPage", "resale/resalereg");
		return "index";
	}

	@GetMapping("/resale-product")
	public String resaleProduct(@RequestParam("no") int pno, HttpServletRequest req) {
		mDAO.isLogined(req);
		rDAO.getAllCategories(req);
		rDAO.getResaleDetail(req, pno);
        req.setAttribute("contentPage", "resale/resaleproduct");
		return "index";
	}
	
	@PostMapping("/resale-reg")
	public String ResaleReg(Resale r, HttpServletRequest req, @RequestParam("files") MultipartFile[] file) {
		mDAO.isLogined(req);
		rDAO.resaleReg(r, req, file);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
}
