package com.minho.ownit.auction;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuctionRepo extends CrudRepository<Auction, Integer>{
	public abstract List<Auction> findAll();
	// 현재 시각(now)보다 auction_end가 이전이고, 상태가 'start'인 것
	public abstract List<Auction> findByEndBeforeAndStatus(Date now, String status);
	
	public abstract List<Auction> findByStatus(String status);

}
