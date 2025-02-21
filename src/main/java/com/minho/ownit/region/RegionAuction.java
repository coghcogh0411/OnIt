package com.minho.ownit.region;

import com.minho.ownit.auction.Auction;

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
@Entity(name = "region_auction")
public class RegionAuction {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_auction_seq")
	@SequenceGenerator(name = "region_auction_seq", sequenceName = "seq_auction_no", allocationSize = 1)
	@Column(name = "region_auction_no")
	private Integer no;
	
	@ManyToOne
	@JoinColumn(name = "region_name")
	private Region region;
	
	@ManyToOne
	@JoinColumn(name = "auction_no")
	private Auction auctionNo;
}
