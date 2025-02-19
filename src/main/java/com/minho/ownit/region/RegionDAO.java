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
	
	public void regionAttribute(HttpServletRequest req, RegionMember user) {
        req.setAttribute("userRegion", user);
    }
	
	public RegionMember regionReg(HttpServletRequest req) {
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
	        String regionParam = req.getParameter("region");
	        if (regionParam == null || regionParam.isEmpty()) {
	            throw new RuntimeException("지역 파라미터가 없습니다.");
	        }

	        // 4) regionParam 파싱
	        Region region = null;
	        if (regionParam.contains(",")) {
	            // 예: "서울특별시, 강남구"
	            String[] arr = regionParam.split(", ");
	            if (arr.length < 2) {
	                throw new RuntimeException("지역 파라미터 형식이 올바르지 않습니다. (예: '서울특별시, 강남구')");
	            }
	            String first = arr[0].trim();
	            String second = arr[1].trim();

	            List<Region> found = rRepo.findByFirstNameAndSecondName(first, second);
	            if (found.isEmpty()) {
	                throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + first + ", " + second + ")");
	            }
	            region = found.get(0);
	        } else {
	            // 예: "강남구" 만 입력되었다면 secondName만 검색
	            List<Region> found = rRepo.findBySecondName(regionParam);
	            if (found.isEmpty()) {
	                throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + regionParam + ")");
	            }
	            region = found.get(0);
	        }

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

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return result; // <- 최종 RegionMember 반환
	}
}
