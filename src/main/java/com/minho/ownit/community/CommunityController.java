package com.minho.ownit.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class CommunityController {
	@Autowired
	private MemberDAO mDAO;
	@Autowired
	private CommunityDAO cDAO;
	
	@GetMapping("/community")
	public String CommunityHome(HttpServletRequest req) {
		mDAO.isLogined(req);
		cDAO.searchClear(req);
		cDAO.get(req, 1);
		req.setAttribute("contentPage", "community/communityhome");
		return "index";
	}
	@GetMapping("/community-page")
	public String CommunityPage(HttpServletRequest req, @RequestParam("page") int page) {
		mDAO.isLogined(req);
		cDAO.get(req, page);
		req.setAttribute("contentPage", "community/communityhome");
		return "index";
	}
	@GetMapping("/community-reg")
	public String CommunityReg(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "community/communityreg");
		return "index";
	}
	@GetMapping("/community-post-reg")
	public String CommunityPostReg(Community c, HttpServletRequest req) {
		mDAO.isLogined(req);
		cDAO.writePost(c, req);
		req.setAttribute("contentPage", "community/communityhome");
		return "index";
	}
	
	@GetMapping("/community-detail")
	public String communityDetail(HttpServletRequest req, Community c) {
		mDAO.isLogined(req);
		cDAO.getDetail(c, req);
		req.setAttribute("contentPage", "community/communitydetail");
		return "index";
	}
	@GetMapping("/reply-reg")
	public String replyReg(HttpServletRequest req, CommunityReply cr, Community c) {
		mDAO.isLogined(req);
		cDAO.writeReply(cr, c, req);
		cDAO.getDetail(c, req);
		req.setAttribute("contentPage", "community/communitydetail");
		return "index";
	}
	
	
}
