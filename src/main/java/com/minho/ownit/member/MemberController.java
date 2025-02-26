package com.minho.ownit.member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import jakarta.servlet.http.HttpServletRequest;




@Controller
public class MemberController {
	@Autowired
	private MemberDAO mDAO;
	
	@GetMapping("Img/{name}")
	public @ResponseBody Resource img(@PathVariable("name") String n, HttpServletRequest req) {
		return mDAO.getImage(n);
	}
	
	@GetMapping("/join")
	public String join(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "member/join");
		return "index";
	}
	@GetMapping("/selectjoin")
	public String selectlogin(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "member/selectjoin");
		return "index";
	}
	@GetMapping("/login")
	public String login(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "member/login");
		return "index";
	}
	
	@GetMapping("/logout")
	public String logout(HttpServletRequest req) {
		mDAO.logout(req);
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return "index";
	}
	
	
	@PostMapping("/sign-in")
	public String loginDo(Member m, HttpServletRequest req) {
		mDAO.memberLogin(m, req);
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return "index";
	}
	
	@GetMapping("/member-home")
	public String memberhome(HttpServletRequest req) {
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "member/memberhome");
		req.setAttribute("myPageContent", "member/update");
		return "index";
	}
	@PostMapping("/sign-up")
	public String SignUp(Member m, HttpServletRequest req, @RequestParam("photoTemp")MultipartFile file ) {
		mDAO.memberReg(m, req, file);
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return "index";
	}
	
	@PostMapping("/update-member")
	public String updateMember(Member m, HttpServletRequest req, @RequestParam("photoTemp")MultipartFile file ) {
		mDAO.update(m, req, file);
		mDAO.isLogined(req);
		req.setAttribute("contentPage", "home");
		return logout(req);
	}
	
}
