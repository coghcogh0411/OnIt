package com.minho.ownit.region;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepo extends CrudRepository<Region, String>{
	public abstract List<Region> findByNameContaining(String name);
	
	public abstract List<Region> findByName(String name);
}
