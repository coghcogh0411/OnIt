package com.minho.ownit.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class AuctionController {
	@Autowired
	private MemberDAO mDAO;
	
	@Autowired
	private AuctionDAO aDAO;
	
	@GetMapping("/auction")
	public String AuctionHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAllAuctionItems(req);
		req.setAttribute("contentPage", "auction/auctionhome");
		return "index";
	}
	@GetMapping("/auction-go-reg")
	public String AuctionGoReg(HttpServletRequest req) {		
		mDAO.isLogined(req);
        req.setAttribute("contentPage", "auction/auctionreg");
		return "index";
	}
	@PostMapping("/auction-reg")
	public String AuctionReg(Auction a, HttpServletRequest req, @RequestParam("files") MultipartFile[] file) {
		mDAO.isLogined(req);
		aDAO.auctionReg(a, req, file);
		req.setAttribute("contentPage", "auction/auctionhome");
		return "index";
	}
	@GetMapping("/auction-product")
	public String resaleProduct(@RequestParam("no") int pno, HttpServletRequest req) {
		mDAO.isLogined(req);
		aDAO.getAuctionDetail(req, pno);
        req.setAttribute("contentPage", "auction/auctionproduct");
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
		req.setAttribute("contentPage", "auction/auctionproduct");
		return "index";
	}
	
}
