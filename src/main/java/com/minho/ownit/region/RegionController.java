package com.minho.ownit.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minho.ownit.member.Member;
import com.minho.ownit.member.MemberDAO;

import jakarta.servlet.http.HttpServletRequest;


@Controller
public class RegionController {
	@Autowired
	private RegionDAO rDAO;
	
	@Autowired
	private MemberDAO mDAO;
	
	@GetMapping(value = "/region.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Regions regionGet(@RequestParam String region) {
		System.out.println("요청된 지역: " + region);
		return rDAO.RegionGet(region);
	}
	
	@GetMapping("/region-get")
    public String regionReg(@RequestParam(value="from") String from,HttpServletRequest req) {

        // 1) 로그인 체크
        mDAO.isLogined(req);  // 세션에 loginMember가 없으면 로그인 페이지 안내 등

        // 2) DB에 region_user 매핑 저장
        //    (실제 로직은 RegionDAO.RegionReg() 내부에서 req.getParameter("region")로 사용)
        rDAO.RegionReg(req);

        // 3) contentPage 동적 지정
        if ("home".equals(from)) {
            req.setAttribute("contentPage", "resale/resalehome");
        } else if ("product".equals(from)) {
            req.setAttribute("contentPage", "resale/resaleproduct");
        } else if ("reg".equals(from)) {
            req.setAttribute("contentPage", "resale/resalereg");
        } else {
            // 기본값 (예: home)
            req.setAttribute("contentPage", "resale/resalehome");
        }

        // 4) index.html (레이아웃)로 이동
        //    index.html 내부에서 <div th:insert="${contentPage}"></div> 등으로 부분화면 삽입
        return "index";
    }
}
