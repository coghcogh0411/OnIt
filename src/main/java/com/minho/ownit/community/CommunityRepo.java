package com.minho.ownit.community;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends CrudRepository<Community, Integer>{

}
