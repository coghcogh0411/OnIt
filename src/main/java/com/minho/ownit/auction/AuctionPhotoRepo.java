package com.minho.ownit.auction;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionPhotoRepo extends CrudRepository<AuctionPhoto, Integer>{
	
}
