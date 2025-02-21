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
	public String regionReg(@RequestParam(value = "from") String f, HttpServletRequest req) {
		if (mDAO.isLogined(req)) {
			mDAO.isLogined(req);
			rgDAO.regionReg(req);
		} else {
			mDAO.isLogined(req);
			rgDAO.noLoginRegion(req);
		}

		if ("home".equals(f)) {
			return "redirect:/resale";
		} else if ("product".equals(f)) {
			// 실제 상품 번호는 "no" 파라미터로 전달되고 있음
			String productNo = req.getParameter("no");
			if (productNo == null || productNo.isEmpty()) {
				// 파라미터가 없으면 기본적으로 홈으로 리다이렉트
				return "redirect:/resale";
			}
			return "redirect:/resale-product?no=" + productNo;
		} else if ("resalereg".equals(f)) {
			return "redirect:/resale-go-reg";
		} else {
			return "redirect:/auction-go-reg";
		}
	}
}
