package com.minho.ownit.region;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionResaleRepo extends CrudRepository<RegionResale, Integer>{
	
	public abstract List<RegionResale> findByRegion_Name(String regionName);
}
