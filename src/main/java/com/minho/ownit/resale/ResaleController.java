package com.minho.ownit.resale;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.ItemLike;
import com.minho.ownit.auction.Auction;
import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class ResaleController {
	@Autowired
	private MemberDAO mDAO;
	
	@Autowired
	private ResaleDAO rsDAO;
	
	
	@GetMapping("/resale-home")
	public String resaleHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		rsDAO.getAllResaleItems(req);
		req.setAttribute("contentPage", "resale/resalehome");
		return "index";
	}
	
	@GetMapping("/resale-product")
    public String resaleProduct(HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        rsDAO.getAllResaleItems(req);
        req.setAttribute("contentPage", "resale/resaleproduct");
        return "index";
    }
	
	@GetMapping("/resale-category")
    public String getResaleByCategory(@RequestParam("no") int pno, HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getResaleByCategory(req, pno);
        req.setAttribute("contentPage", "resale/resaleproduct");
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
	

	@GetMapping("/resale-detailproduct")
    public String resaleDetailProduct(@RequestParam("no") int pno, HttpServletRequest req) {
        mDAO.isLogined(req);
        rsDAO.setDisplayRegion(req);
        rsDAO.getAllCategories(req);
        rsDAO.getResaleDetail(req, pno);
        req.setAttribute("contentPage", "resale/resaledetailproduct");
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
        req.setAttribute("contentPage", "resale/resaleproduct");
        return "index";
    }
	@GetMapping("/resale-like")
	public String auctionLike(HttpServletRequest req, Auction a, ItemLike il, @RequestParam("pno") int no) {
		mDAO.isLogined(req);
		rsDAO.setDisplayRegion(req);
		rsDAO.getAllCategories(req);
		rsDAO.like(il, req);
		rsDAO.getResaleDetail(req, no);
		req.setAttribute("contentPage", "resale/resaledetailproduct");
		return "index";
	}
	@GetMapping("/resale-liked")
	public String auctionLiked(HttpServletRequest req, Auction a, ItemLike il, @RequestParam("pno") int no) {
		mDAO.isLogined(req);
		rsDAO.setDisplayRegion(req);
		rsDAO.getAllCategories(req);
		rsDAO.liked(il, req);
		rsDAO.getResaleDetail(req, no);
		req.setAttribute("contentPage", "resale/resaledetailproduct");
		return "index";
	}
}
