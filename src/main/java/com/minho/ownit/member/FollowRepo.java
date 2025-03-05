package com.minho.ownit.member;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepo extends CrudRepository<Follow, Integer>{
	public abstract List<Follow> findByFollower(Member follower);
	public abstract List<Follow> findByFollowing(Member following);
}
