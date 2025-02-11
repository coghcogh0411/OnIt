package com.minho.ownit.community;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityRepo extends CrudRepository<Community, Integer>{
	public abstract List<Community> findByTitleContainingOrWriterIdContaining(Pageable pg, String title, String id);
	public abstract Long countByTitleContainingOrWriterIdContaining(String title, String id);
}
