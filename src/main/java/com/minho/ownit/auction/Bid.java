package com.minho.ownit.auction;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import com.minho.ownit.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "bid")
public class Bid {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bid_seq")
    @SequenceGenerator(name = "bid_seq", sequenceName = "seq_bid_no", allocationSize = 1)
    @Column(name = "bid_no")
	private Integer bidNo;
	@ManyToOne
	@JoinColumn(name = "auction_no")
	private Auction auctionNo;
	@ManyToOne
	@JoinColumn(name = "user_id")	
	private Member user;
	@Column(name = "bid_amount")
	private Integer amount;
	@UpdateTimestamp
	@Column(name = "bid_time")
	private Date bidTime;
}
