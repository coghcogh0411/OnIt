package com.minho.ownit.community;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.minho.ownit.member.Member;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class CommunityDAO {
	@Autowired
	private CommunityRepo cRepo;
	@Autowired
	private CommunityReplyRepo crRepo;
	
	private int postPerPage;
	
	public CommunityDAO() {
		// TODO Auto-generated constructor stub
		postPerPage = 5;
	}
	
	public void writePost(Community c, HttpServletRequest req) {
		try {
			String token = req.getParameter("token");
			String oldToken = (String) req.getSession().getAttribute("successToken");
			if (oldToken != null && token.equals(oldToken)) {
				req.setAttribute("result", "글쓰기 실패(새로고침)");
				return;
			}
			Member m = (Member) req.getSession().getAttribute("loginMember");
			c.setWriter(m);
			c.setContent(c.getContent().replace("\r\n", "<br>"));
			
			c.setLike(0);
			c.setComment(0);
			c.setView(0);
			cRepo.save(c);

			req.setAttribute("result", "글쓰기 성공");
			req.getSession().setAttribute("successToken", token);

		} catch (Exception e) {
			e.printStackTrace();
			req.setAttribute("result", "글쓰기 실패");
		}
	}
	
	public void writeReply(CommunityReply cr, Community c, HttpServletRequest req) {
		try {
			String token = req.getParameter("token");
			String oldToken = (String) req.getSession().getAttribute("successToken");
			if (oldToken != null && token.equals(oldToken)) {
				req.setAttribute("result", "글쓰기 실패(새로고침)");
				return;
			}
			Member m = (Member) req.getSession().getAttribute("loginMember");
			cr.setWriter(m);
			cr.setPno(cRepo.findById(c.getNo()).get());
			crRepo.save(cr);
			
			req.setAttribute("result", "글쓰기 성공");
			req.getSession().setAttribute("successToken", token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void get(HttpServletRequest req, int page) {
		try {
			String searchTitle = (String) req.getSession().getAttribute("search");
			long postCount = cRepo.count();
			if (searchTitle == null) {
				searchTitle = "";
			} else {
				postCount = cRepo.countByTitleContainingOrWriterIdContaining(searchTitle, searchTitle);
			}
			int pageCount = (int) Math.ceil((double) postCount / postPerPage);
			req.setAttribute("pageCount", pageCount);
			req.setAttribute("curPage", page);
			Sort sort = Sort.by(Sort.Order.desc("date"));
			Pageable pg = PageRequest.of(page - 1, postPerPage, sort);
			req.setAttribute("post", cRepo.findByTitleContainingOrWriterIdContaining(pg, searchTitle, searchTitle));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getDetail(Community c, HttpServletRequest req) {
		Integer postNo = Integer.parseInt(req.getParameter("no"));
		Community com = cRepo.findById(postNo).get();
		req.setAttribute("postdetail", com);
		req.setAttribute("reply", crRepo.findAll());
	}

	public void search(HttpServletRequest req) {
		String searchTxt = req.getParameter("keyword");
		req.getSession().setAttribute("search", searchTxt);
	}

	public void searchClear(HttpServletRequest req) {
		req.getSession().setAttribute("search", null);
	}
	
}
