package com.minho.ownit.region;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class RegionController {
	@Autowired
	private RegionDAO rDAO;
	
	@GetMapping(value = "/region.get", produces = "application/json;charset=utf-8")
	public @ResponseBody Regions regionGet(@RequestParam String region) {
		System.out.println("요청된 지역: " + region);
		return rDAO.RegionGet(region);
	}
	
	@GetMapping("/region-get")
	public String regionReg(@RequestParam String param) {
		return new String();
	}
	
	
}
