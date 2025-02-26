package com.minho.ownit;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.minho.ownit.auction.Auction;
import com.minho.ownit.member.Member;
import com.minho.ownit.resale.Resale;

@Repository
public interface ItemLikeRepo extends CrudRepository<ItemLike, Integer>{
	public abstract ItemLike findByUserIdAndAuctionNo(Member userId, Auction auctionNo);
	public abstract ItemLike findByUserIdAndResaleNo(Member userId, Resale resaleNo);
	
	// 1) "사용자 + 경매글" 좋아요 여부 확인
    public abstract boolean existsByUserIdAndAuctionNo(Member userId, Auction auctionNo);

    // 2) "사용자 + 중고거래글" 좋아요 여부 확인
    public abstract boolean existsByUserIdAndResaleNo(Member userId, Resale resaleNo);
}
