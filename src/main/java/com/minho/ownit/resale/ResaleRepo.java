package com.minho.ownit.resale;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.minho.ownit.member.Member;

public interface ResaleRepo extends CrudRepository<Resale, Integer>{

	public abstract List<Resale> findByNo(int no);
	public abstract List<Resale> findByUser(Member id);
}
