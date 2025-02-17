package com.minho.ownit.auction;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.resale.Resale;

@Repository
public interface AuctionRepo extends CrudRepository<Auction, Integer>{
	public abstract List<Auction> findAll();
}
