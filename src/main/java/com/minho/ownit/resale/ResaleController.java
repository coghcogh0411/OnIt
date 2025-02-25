package com.minho.ownit.resale;



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
	private ResaleDAO rsDAO;
	
	@GetMapping("/resale")
    public String resaleHome(HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        rsDAO.getAllResaleItems(req);
        req.setAttribute("contentPage", "resale/resalehome");
        return "index";
    }
	
	@GetMapping("/resale-category")
    public String getResaleByCategory(@RequestParam("no") int pno, HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getResaleByCategory(req, pno);
        req.setAttribute("contentPage", "resale/resalehome");
        return "index";
    }
	
	
	@GetMapping("/resale-go-reg")
    public String resaleGoReg(HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        req.setAttribute("contentPage", "resale/resalereg");
        return "index";
    }
	

	@GetMapping("/resale-product")
    public String resaleProduct(@RequestParam("no") int pno, HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        rsDAO.getResaleDetail(req, pno);
        req.setAttribute("contentPage", "resale/resaleproduct");
        return "index";
    }
	
	@PostMapping("/resale-reg")
    public String resaleReg(Resale r, HttpServletRequest req,
                            @RequestParam("files") MultipartFile[] photos) {
        mDAO.isLogined(req);
        rsDAO.resaleReg(r, req, photos);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        rsDAO.getAllResaleItems(req);
        req.setAttribute("contentPage", "resale/resalehome");
        return "index";
    }
	
}
