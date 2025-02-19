package com.minho.ownit.region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

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
	
	public Regions RegionGet(String r) {
		
		List<Region> regions = rRepo.findByNameContaining(r);
		return new Regions(regions);
	}
	
	public void RegionReg(HttpServletRequest req) {
        try {
            // 1) 세션에서 로그인 사용자 가져오기
            Member loginMember = (Member) req.getSession().getAttribute("loginMember");
            if (loginMember == null) {
                throw new RuntimeException("로그인되지 않았습니다.");
            }

            // 2) user DB 조회 (세션 객체가 영속상태가 아닐 수 있으므로 다시 조회)
            Member user = mRepo.findById(loginMember.getId())
                    .orElseThrow(() -> new RuntimeException("해당 유저가 DB에 없습니다."));

            // 3) 쿼리 파라미터에서 지역명 가져오기
            //    예: "경기도, 부천시" 또는 "부천시"
            String regionParam = req.getParameter("region");
            if (regionParam == null || regionParam.isEmpty()) {
                throw new RuntimeException("지역 파라미터가 없습니다.");
            }

            // 4) 파라미터에 콤마(",")가 있는지 여부에 따라 로직 분기
            Region region = null;
            if (regionParam.contains(",")) {
                // 예: "경기도, 부천시"
                String[] arr = regionParam.split(", ");
                if (arr.length < 2) {
                    throw new RuntimeException("지역 파라미터 형식이 올바르지 않습니다. (예: '서울특별시, 강남구')");
                }
                String first = arr[0].trim();   // 예: "경기도"
                String second = arr[1].trim();  // 예: "부천시"

                // 시/도(firstName), 시/군/구(secondName) 모두 일치하는 레코드 찾기
                List<Region> found = rRepo.findByFirstNameAndSecondName(first, second);
                if (found.isEmpty()) {
                    throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + first + ", " + second + ")");
                }
                region = found.get(0);

            } else {
                // 예: "부천시"
                // 시/군/구(secondName)만 일치하는 레코드 찾기
                List<Region> found = rRepo.findBySecondName(regionParam);
                if (found.isEmpty()) {
                    throw new RuntimeException("해당 지역이 DB에 없습니다. (검색: " + regionParam + ")");
                }
                // "부천시"가 여러 시/도에 걸쳐 있을 수도 있으니 필요 시 추가 로직
                region = found.get(0);
            }

            // 5) region_user 테이블에서 해당 user_id가 이미 있는지 조회
            RegionMember existing = rmRepo.findByUser(user);

            if (existing != null) {
                // === 이미 있으면 UPDATE ===
                existing.setRegion(region);
                rmRepo.save(existing); // UPDATE 쿼리
            } else {
                // === 없으면 INSERT ===
                RegionMember rm = new RegionMember();
                rm.setRegion(region);
                rm.setUser(user);
                rmRepo.save(rm); // INSERT 쿼리
            }

        } catch (Exception e) {
            e.printStackTrace();
            // 필요 시 throw 등
        }
    }
}
