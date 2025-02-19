package com.minho.ownit.auction;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BidRepo extends CrudRepository<Bid, Integer>{
	public abstract List<Bid> findByAuctionNoNoOrderByAmountDesc(int auctionNo);
}
