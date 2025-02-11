package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityReplyRepo extends CrudRepository<CommunityReply, Integer>{
	public abstract List<CommunityReply> findBy();
}
