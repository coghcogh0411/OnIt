package com.minho.ownit.member;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepo extends CrudRepository<Member, String>{

}
