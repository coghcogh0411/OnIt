package com.minho.ownit.resale;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

public interface ResalePhotoRepo extends CrudRepository<ResalePhoto, Integer>{

	public abstract List<ResalePhoto> findByResale_no(int no);
}
