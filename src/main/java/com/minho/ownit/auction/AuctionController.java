package com.minho.ownit.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.ItemLike;
import com.minho.ownit.member.MemberDAO;
import com.minho.ownit.resale.ResaleDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuctionController {
	@Autowired
	private MemberDAO mDAO;
	
	@Autowired
	private AuctionDAO aDAO;
	
	@Autowired
	private ResaleDAO rsDAO;
	
	@GetMapping("/auction-home")
	public String auctionHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAuctionHome(req);
		req.setAttribute("contentPage", "auction/auctionhome");
		return "index";
	}
	
	@GetMapping("/auction-product")
	public String AuctionProduct(HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAllStartAuctionItems(req);
		req.setAttribute("contentPage", "auction/auctionproduct");
		return "index";
	}
	
	@GetMapping("/auction-go-reg")
	public String AuctionGoReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
		rsDAO.setDisplayRegion(req);
        req.setAttribute("contentPage", "auction/auctionreg");
		return "index";
	}
	@PostMapping("/auction-reg")
	public String AuctionReg(Auction a, HttpServletRequest req, @RequestParam("files") MultipartFile[] file) {
		mDAO.isLogined(req);
		aDAO.auctionReg(a, req, file);
		aDAO.getAllStartAuctionItems(req);
		req.setAttribute("contentPage", "auction/auctionproduct");
		return "index";
	}
	@GetMapping("/auction-detailproduct")
	public String auctionDetailProduct(@RequestParam("no") int pno, HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAuctionDetail(req, pno);
        req.setAttribute("contentPage", "auction/auctiondetailproduct");
		return "index";
	}
	
	@GetMapping(value = "/bid.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Bids bidGet(@RequestParam String auctionNo) {
		System.out.println("요청된 번호: " + auctionNo);
		return aDAO.BidGet(auctionNo);
	}
	
	@GetMapping("/auction-bid")
	public String Bid(@RequestParam("auctionNo") int no, Bid b, HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAuctionDetail(req, no);
		aDAO.Bid(b, req);
		req.setAttribute("contentPage", "auction/auctiondetailproduct");
		return "index";
	}
	
	@GetMapping("/pastauction-product")
    public String pastAuctionProduct(HttpServletRequest req) {
        mDAO.isLogined(req);
        aDAO.getAllEndAuctionItems(req);
        req.setAttribute("contentPage", "auction/pastauctionproduct");
        return "index";
    }
	
	@GetMapping("/pastauction-detailproduct")
	public String pastAuctionDetailProduct(@RequestParam("no") int pno, HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAuctionDetail(req, pno);
        req.setAttribute("contentPage", "auction/auctiondetailproduct");
		return "index";
	}
	
	@GetMapping("/auction-like")
	public String auctionLike(HttpServletRequest req, Auction a, ItemLike il, @RequestParam("pno") int no) {
		mDAO.isLogined(req);
		aDAO.like(il, req);
		aDAO.getAuctionDetail(req,no);
		req.setAttribute("contentPage", "auction/auctiondetailproduct");
		return "index";
	}
	@GetMapping("/auction-liked")
	public String auctionLiked(HttpServletRequest req, Auction a, ItemLike il, @RequestParam("pno") int no) {
		mDAO.isLogined(req);
		aDAO.liked(il, req);
		aDAO.getAuctionDetail(req,no);
		req.setAttribute("contentPage", "auction/auctiondetailproduct");
		return "index";
	}
	
}
