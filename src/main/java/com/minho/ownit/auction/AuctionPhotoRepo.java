package com.minho.ownit.auction;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.resale.ResalePhoto;

@Repository
public interface AuctionPhotoRepo extends CrudRepository<AuctionPhoto, Integer>{
	public abstract List<AuctionPhoto> findByAuction_no(int no);
}
