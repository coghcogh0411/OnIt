package com.minho.ownit.region;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.member.Member;

@Repository
public interface RegionMemberRepo extends CrudRepository<RegionMember, Integer>{
	public abstract RegionMember findByUser(Member user);
}
