package com.minho.ownit.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.minho.ownit.member.Member;
import com.minho.ownit.member.MemberDAO;
import com.minho.ownit.resale.ResaleDAO;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class RegionController {
	@Autowired
	private RegionDAO rgDAO;

	@Autowired
	private MemberDAO mDAO;

	@Autowired
	private ResaleDAO rsDAO;

	@GetMapping(value = "/region.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Regions regionGet(@RequestParam String region) {
		System.out.println("요청된 지역: " + region);
		return rgDAO.regionGet(region);
	}

	@GetMapping("/region-get")
	public String regionReg(@RequestParam(value = "from") String from, HttpServletRequest req) {
		if (mDAO.isLogined(req)) {
			mDAO.isLogined(req);
	        rgDAO.regionReg(req);
	    } else {
	    	mDAO.isLogined(req);
	        rgDAO.noLoginRegion(req);
	    }
        rsDAO.getAllCategories(req);
        rsDAO.getAllResaleItems(req);
        
        // 4) 어느 화면으로 이동할지 결정 (from 파라미터)
        if ("home".equals(from)) {
            req.setAttribute("contentPage", "resale/resalehome");
        } else if ("product".equals(from)) {
            req.setAttribute("contentPage", "resale/resaleproduct");
        } else if ("reg".equals(from)) {
            req.setAttribute("contentPage", "resale/resalereg");
        } else {
            // 기본값: home
            req.setAttribute("contentPage", "resale/resalehome");
        }
		return "index";
	}
}
