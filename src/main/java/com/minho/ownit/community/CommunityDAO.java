package com.minho.ownit.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minho.ownit.member.Member;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommunityDAO {
	@Autowired
	private CommunityRepo cRepo;
	
	@Value("${ho.img.folder}")
	private String imgFolder;
	
	public void writePost(Community s, HttpServletRequest req) {
		try {
			String token = req.getParameter("token");
			String oldToken = (String) req.getSession().getAttribute("successToken");
			if (oldToken != null && token.equals(oldToken)) {
				req.setAttribute("result", "글쓰기 실패(새로고침)");
				return;
			}
			Member m = (Member) req.getSession().getAttribute("loginMember");
			s.setId(m);
			s.setContent(s.getContent().replace("\r\n", "<br>"));

			cRepo.save(s);

			req.setAttribute("result", "글쓰기 성공");
			req.getSession().setAttribute("successToken", token);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "글쓰기 실패");
		}
	}
}
