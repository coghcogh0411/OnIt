package com.minho.ownit.region;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionMemberRepo extends CrudRepository<RegionMember, Integer>{

}
