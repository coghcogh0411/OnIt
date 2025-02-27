package com.minho.ownit.auction;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.member.Member;

@Repository
public interface AuctionRepo extends CrudRepository<Auction, Integer>{
	public abstract List<Auction> findAll();
	
	public abstract Auction findByNo(int no);
	
	// 현재 시각(now)보다 auction_end가 이전이고, 상태가 'start'인 것
	public abstract List<Auction> findByEndBeforeAndStatus(Date now, String status);
	
	public abstract List<Auction> findByStatus(String status);
	
	public abstract List<Auction> findByUser(Member nickname);

}
