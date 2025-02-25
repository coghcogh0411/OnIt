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
        try {
            // 1) 세션에서 로그인 사용자 가져오기
            Member loginMember = (Member) req.getSession().getAttribute("loginMember");
            if (loginMember == null) {
                throw new RuntimeException("로그인되지 않았습니다.");
            }

            // 2) DB에서 Member 다시 조회
            Member user = mRepo.findById(loginMember.getId())
                    .orElseThrow(() -> new RuntimeException("해당 유저가 DB에 없습니다."));

            // 3) 파라미터
            String regionParam1 = req.getParameter("region");   // ex) "서울특별시"
            String regionParam2 = req.getParameter("region2");  // ex) "강남구"

            if (regionParam2 == null || regionParam2.isEmpty()) {
                throw new RuntimeException("지역 파라미터가 없습니다.");
            }

            // 3-1) DB 검색용 (예: "서울특별시강남구")
            String hapParam = regionParam1 + regionParam2;

            // 3-2) 세션 표시용 (예: "서울특별시, 강남구")
            String displayParam = regionParam1 + ", " + regionParam2;
            req.getSession().setAttribute("regionSession", displayParam);

            // 4) DB 검색
            List<Region> found = rRepo.findByName(hapParam);
            if (found.isEmpty()) {
                throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + hapParam + ")");
            }
            Region region = found.get(0);

            // 5) region_user 테이블에서 user_id로 조회
            RegionMember existing = rmRepo.findByUser(user);

            // 6) INSERT or UPDATE
            if (existing != null) {
                existing.setRegion(region);
                rmRepo.save(existing);
            } else {
                RegionMember rm = new RegionMember();
                rm.setRegion(region);
                rm.setUser(user);
                rmRepo.save(rm);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	
	public void noLoginRegion(HttpServletRequest req) {
        try {
            String regionParam1 = req.getParameter("region");  // ex) "광주광역시"
            String regionParam2 = req.getParameter("region2"); // ex) "서구"
            if (regionParam1 == null || regionParam1.isEmpty()) return; 
            if (regionParam2 == null || regionParam2.isEmpty()) return; 

            // 검색용 => "광주광역시서구" (DB region_name과 일치)
            String hapParamSearch = regionParam1 + regionParam2;

            // 표시용 => "광주광역시, 서구"
            String hapParamDisplay = regionParam1 + ", " + regionParam2;

            req.getSession().setAttribute("regionSessionSearch", hapParamSearch);
            req.getSession().setAttribute("regionSessionDisplay", hapParamDisplay);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	
}
