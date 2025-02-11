package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCategoryRepo extends CrudRepository<CommunityCategory, Integer>{
	public abstract List<CommunityCategory> findAll();
}
