package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityLikeRepo extends CrudRepository<CommunityLike, Integer>{
	public abstract List<CommunityLike> findAll();
}
