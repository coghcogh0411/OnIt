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

import com.minho.ownit.HomeController;

import jakarta.servlet.http.HttpServletRequest;




@Controller
public class MemberController {
	@Autowired
	private MemberDAO mDAO;
	@Autowired
	private HomeController hCon;
	
	
	@GetMapping(value = "/member.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Members memberGet(@RequestParam String m) {
		return mDAO.getMemberToJSON(m);
	}
	
	@GetMapping(value = "/membernickname.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Member memberNicknameGet(@RequestParam String m) {
		return mDAO.getMemberNicknameToJSON(m);
	}
	
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
	public String memberhome(HttpServletRequest req, @RequestParam("name") String nickname) {
		mDAO.isLogined(req);
		mDAO.memberPage(req, nickname);
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
	
	@GetMapping("/member-product")
	public String memberProduct(HttpServletRequest req,
	                            @RequestParam("name") String nickname,
	                            @RequestParam(value = "filter", required = false) String f) {
	    mDAO.isLogined(req);
	    mDAO.memberProduct(req, nickname, f);
	    return "index";
	}
	
	@GetMapping("/member-like")
	public String memberlike(HttpServletRequest req, @RequestParam("name") String nickname,
	                         @RequestParam(value="filter", required=false) String f) {
	    mDAO.isLogined(req);
	    mDAO.memberLike(req, nickname, f);
	    return "index";
	}

	
	@GetMapping("/member-follow")
	public String memberfollow(HttpServletRequest req, @RequestParam("name") String nickname) {
		mDAO.isLogined(req);
		mDAO.memberfollow(req, nickname);
		req.setAttribute("contentPage", "member/memberhome");
		req.setAttribute("myPageContent", "member/follow");
		return "index";
	}
	
	@GetMapping("/member-delete")
	public String memberDelete(HttpServletRequest req) {
		mDAO.memberDelete(req);
		logout(req);
		mDAO.isLogined(req);
		
		req.setAttribute("contentPage", "home");
		return "index";
	}
	
	
}
