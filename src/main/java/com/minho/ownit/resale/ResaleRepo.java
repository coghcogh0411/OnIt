package com.minho.ownit.resale;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ResaleRepo extends CrudRepository<Resale, Integer>{

	public abstract List<Resale> findByCategory_No(int no);
}
