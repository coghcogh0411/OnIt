package com.minho.ownit.community;

import java.util.List;

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
	@Autowired
	private CommunityCategoryRepo ccRepo;

	private int postPerPage;

	public CommunityDAO() {
		// TODO Auto-generated constructor stub
		postPerPage = 5;
		
	}
	
	public void categoryselect(HttpServletRequest req) {
		req.setAttribute("category", ccRepo.findAll());
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
			// 세션에서 검색어와 카테고리 번호 가져오기
	        String searchTitle = (String) req.getSession().getAttribute("search");
	        Integer categoryNo = (Integer) req.getSession().getAttribute("category"); 

	        // URL 파라미터로 카테고리 번호가 있으면 세션 카테고리 번호를 덮어씌우기
	        if (req.getParameter("categoryNo") != null) {
	            categoryNo = Integer.parseInt(req.getParameter("categoryNo"));
	            req.getSession().setAttribute("category", categoryNo);
	        }
	        
	        // categoryNo가 null이면 기본값 1 설정
	        if (categoryNo == null) {
	            categoryNo = 1;
	            req.getSession().setAttribute("category", categoryNo);
	        }

	        System.out.println("현재 카테고리: " + categoryNo); // 디버깅 로그

	        long postCount = 0;

	        // 카테고리별 게시글 수 조회
	        if (searchTitle == null || searchTitle.isEmpty()) {
	            postCount = cRepo.countByCategory_CategoryNo(categoryNo);
	        } else {
	            postCount = cRepo.countByCategory_CategoryNoAndTitleContainingOrWriterIdContaining(
	                    categoryNo, searchTitle, searchTitle);
	        }

	        // 페이지 개수 계산
	        int pageCount = (int) Math.ceil((double) postCount / postPerPage);
	        req.setAttribute("pageCount", pageCount);
	        req.setAttribute("curPage", page);

	        // 페이지 설정
	        Sort sort = Sort.by(Sort.Order.desc("date"));
	        Pageable pg = PageRequest.of(page - 1, postPerPage, sort);

	        // 카테고리별 게시글 조회
	        List<Community> posts;
	        if (searchTitle == null || searchTitle.isEmpty()) {
	            posts = cRepo.findByCategory_CategoryNo(categoryNo, pg);
	        } else {
	            posts = cRepo.findByCategory_CategoryNoAndTitleContainingOrWriterIdContaining(
	                    categoryNo, searchTitle, searchTitle, pg);
	        }
	        req.setAttribute("categoryname", ccRepo.findById(categoryNo));
	        req.setAttribute("post", posts);
	        req.setAttribute("category", ccRepo.findAll()); // 카테고리 목록 설정

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getDetail(Community c, HttpServletRequest req) {
		Integer postNo = Integer.parseInt(req.getParameter("no"));
		Community com = cRepo.findById(postNo).get();
		req.setAttribute("postdetail", com);
		req.setAttribute("reply", ccRepo.findAll());

	}

	public void search(HttpServletRequest req) {
		String searchTxt = req.getParameter("keyword");
		req.getSession().setAttribute("search", searchTxt);
	}

	public void searchClear(HttpServletRequest req) {
		req.getSession().setAttribute("search", null);
	}

}
