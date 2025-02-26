package com.minho.ownit;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.minho.ownit.auction.Auction;
import com.minho.ownit.member.Member;
import com.minho.ownit.resale.Resale;

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

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity(name = "item_like")
public class ItemLike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_item_like_no")
	@SequenceGenerator(name = "seq_item_like_no", sequenceName = "seq_item_like_no", allocationSize = 1)
	@Column(name = "like_no")
	private Integer likeNo;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member userId;
	@ManyToOne
	@JoinColumn(name = "auction_no")
	private Auction auctionNo;
	@ManyToOne
	@JoinColumn(name = "resale_no")
	private Resale resaleNo;
	@Column(name = "item_type")
	private String itemType;
	@CreationTimestamp
	@Column(name = "like_time")
	private Date likeTime;
}
