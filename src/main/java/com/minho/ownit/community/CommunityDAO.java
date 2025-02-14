package com.minho.ownit.community;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
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
	@Autowired
	private CommunityLikeRepo clRepo;
	
	@Value("${ho.img.folder}")
	private String imgFolder;
	
	private int postPerPage;

	public CommunityDAO() {
		// TODO Auto-generated constructor stub
		postPerPage = 8;
		
	}
	
	public Resource getImage(String n) {
		try {
			return new UrlResource("file:" + imgFolder + "/" + n);

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
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

	public void writeReply(CommunityReply cr, HttpServletRequest req) {
		try {
			String token = req.getParameter("token");
			String oldToken = (String) req.getSession().getAttribute("successToken");
			if (oldToken != null && token.equals(oldToken)) {
				req.setAttribute("result", "글쓰기 실패(새로고침)");
				return;
			}
			Member m = (Member) req.getSession().getAttribute("loginMember");
			cr.setWriter(m);
			Community c = cRepo.findByNo(Integer.parseInt(req.getParameter("pno")));
			cr.setPno(c);
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
	        
	        Map<Integer, Long> likeCountMap = new HashMap<>();
	        for (Community post : posts) {
	            long likeCount = clRepo.countByPost(post);
	            likeCountMap.put(post.getNo(), likeCount);
	        }
	        
			req.setAttribute("likecount", likeCountMap);
	        req.setAttribute("categorytitle", ccRepo.findById(categoryNo).get());
	        req.setAttribute("post", posts);
	        req.setAttribute("category", ccRepo.findAll()); // 카테고리 목록 설정

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getDetail(Community c, HttpServletRequest req) {
		Integer postNo = Integer.parseInt(req.getParameter("pno"));
		Community com = cRepo.findById(postNo).get();
		Member m = (Member)req.getSession().getAttribute("loginMember");
		
		boolean isLike = clRepo.existsByPostAndUser(com, m);
		
		req.setAttribute("isLike", isLike);
		req.setAttribute("likecount", clRepo.countByPost(com));
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
	
	public void like(CommunityLike cl, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			cl.setUser(m);
			int pno = Integer.parseInt(req.getParameter("pno"));
			Community c = cRepo.findByNo(pno);
			cl.setPost(c);
			clRepo.save(cl);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void liked(CommunityLike cl, HttpServletRequest req) {
		try {
			Member m = (Member) req.getSession().getAttribute("loginMember");
			int pno = Integer.parseInt(req.getParameter("pno"));
			Community c = cRepo.findByNo(pno);
			CommunityLike existingLike = clRepo.findByPostAndUser(c, m);
			clRepo.delete(existingLike);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
