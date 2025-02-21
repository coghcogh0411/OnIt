package com.minho.ownit.region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minho.ownit.member.Member;
import com.minho.ownit.member.MemberRepo;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class RegionDAO {
	@Autowired
	private RegionRepo rRepo;
	
	@Autowired
	private RegionMemberRepo rmRepo;
	
	@Autowired
	private MemberRepo mRepo;
	
	public Regions regionGet(String r) {
		
		List<Region> regions = rRepo.findByNameContaining(r);
		return new Regions(regions);
	}
	
	public void regionReg(HttpServletRequest req) {
	    RegionMember result = null;
	    try {
	        // 1) 세션에서 로그인 사용자 가져오기
	        Member loginMember = (Member) req.getSession().getAttribute("loginMember");
	        if (loginMember == null) {
	            throw new RuntimeException("로그인되지 않았습니다.");
	        }

	        // 2) DB에서 Member 다시 조회
	        Member user = mRepo.findById(loginMember.getId())
	                .orElseThrow(() -> new RuntimeException("해당 유저가 DB에 없습니다."));

	        // 3) 쿼리 파라미터 "region" 가져오기
	        String regionParam1 = req.getParameter("region");
	        String regionParam2 = req.getParameter("region2");
	        req.getSession().setAttribute("regionSession", regionParam2);
	        if (regionParam2 == null || regionParam2.isEmpty()) {
	            throw new RuntimeException("지역 파라미터가 없습니다.");
	        }
	        String hapParam = regionParam1+regionParam2;
	        
	        // 4) regionParam 파싱
	        Region region = null;
	        
	            List<Region> found = rRepo.findByName(hapParam);
	            if (found.isEmpty()) {
	                throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + hapParam + ")");
	            }
	            region = found.get(0);

	        // 5) region_user 테이블에서 user_id로 조회
	        RegionMember existing = rmRepo.findByUser(user);

	        // 6) INSERT or UPDATE
	        if (existing != null) {
	            existing.setRegion(region);
	            result = rmRepo.save(existing);
	        } else {
	            RegionMember rm = new RegionMember();
	            rm.setRegion(region);
	            rm.setUser(user);
	            result = rmRepo.save(rm);
	        }
	        req.getSession().getAttribute("regionSession");
	        
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public void noLoginRegion(HttpServletRequest req) {
		String regionParam2 = req.getParameter("region2");
        if (regionParam2 != null && !regionParam2.isEmpty()) {
            req.getSession().setAttribute("regionSession", regionParam2);
        }
	}
}
