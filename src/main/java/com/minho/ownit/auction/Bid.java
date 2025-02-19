package com.minho.ownit.auction;

import java.util.Date;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    @Column(name = "bid_name")
	private String bidName;
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
