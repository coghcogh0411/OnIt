package com.minho.ownit.resale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.minho.ownit.member.Member;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ResaleDAO {
	@Autowired
	private ResaleRepo Rrepo;
	
	@Autowired
	private ResalePhotoRepo RPrepo;

	@Value("${ho.img.folder}")
	private String imgFolder;
	
	public void resaleReg(Resale r, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			r.setUser(m);
			
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	
}
