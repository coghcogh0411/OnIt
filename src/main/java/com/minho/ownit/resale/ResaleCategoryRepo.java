package com.minho.ownit.resale;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResaleCategoryRepo extends CrudRepository<ResaleCategory, Integer>{

	public abstract List<ResaleCategory> findAll();
}
