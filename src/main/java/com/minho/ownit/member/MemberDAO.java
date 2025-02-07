package com.minho.ownit.member;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.minho.ownit.FileNameGenerator;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class MemberDAO {
	@Autowired
	private MemberRepo Mrepo;
	
	@Value("${ho.img.folder}")
	private String imgFolder;
	
	private BCryptPasswordEncoder bcpe;
	private SimpleDateFormat sdf;

	public MemberDAO() {
		bcpe = new BCryptPasswordEncoder();
		sdf = new SimpleDateFormat("yyyyMMdd");
	}
	
	public void memberReg(Member m, HttpServletRequest req, MultipartFile file) {
		try {
			// 비번 암호화
			String dbPw = bcpe.encode(m.getPw());
			m.setPw(dbPw);
			
			String year = req.getParameter("year");
			int month = Integer.parseInt(req.getParameter("month"));
			int day = Integer.parseInt(req.getParameter("day"));
			String bd = String.format("%s%02d%02d", year, month, day);
			Date birthday = sdf.parse(bd);
			m.setBirthday(birthday);

			String phone1 = req.getParameter("phone1");
			String phone2 = req.getParameter("phone2");
			String phone3 = req.getParameter("phone3");
			String phone = phone1 + "-" + phone2 + "-" + phone3;
			m.setPhone(phone);

			String addr1 = req.getParameter("addr1");
			String addr2 = req.getParameter("addr2");
			String addr = addr1 + "*" + addr2;
			m.setAddr(addr);
			
			String fileName = FileNameGenerator.generator(file);
			file.transferTo(new File(imgFolder + "/" + fileName));
			m.setPhoto(fileName);
			
			if (Mrepo.existsById(m.getId())) {
				throw new Exception();
			}
			Mrepo.save(m);
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void memberLogin(Member m, HttpServletRequest req) {
		try {
			Optional<Member> memberTemp = Mrepo.findById(m.getId());
			if (memberTemp.isPresent()) {
				Member dbMember = memberTemp.get();
				if (bcpe.matches(m.getPw(), dbMember.getPw())) {
					req.getSession().setAttribute("loginMember", dbMember);
					req.getSession().setMaxInactiveInterval(10 * 60);
				} else {
					req.setAttribute("result", "로그인 실패(pw오류)");
				}
			} else {
				req.setAttribute("result", "로그인 실패(미가입id)");
			}
		} catch (Exception e) {
			req.setAttribute("result", "로그인 실패(DB)");
		}
		
	}
	
	public boolean isLogined(HttpServletRequest req) {
		Member m = (Member) req.getSession().getAttribute("loginMember");
		if (m != null) {
			req.setAttribute("loginPage", "member/logined");
			return true;
		}
		req.setAttribute("loginPage", "member/loginreg");
		return false;
	}
}
