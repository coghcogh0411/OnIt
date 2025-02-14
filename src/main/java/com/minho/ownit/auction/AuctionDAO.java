package com.minho.ownit.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuctionDAO {
	@Autowired
	private AuctionRepo aRepo;
	public void getAllResaleItems(HttpServletRequest req) {
		
		req.setAttribute("auctionList", aRepo.findAll());
	}
}
