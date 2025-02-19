package com.minho.ownit.region;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.minho.ownit.member.MemberRepo;

@Service
public class RegionDAO {
	@Autowired
	private RegionRepo rRepo;
	
	
	public Regions RegionGet(String r) {
		
		List<Region> regions = rRepo.findByNameContaining(r);
		return new Regions(regions);
	}
	
	
}
