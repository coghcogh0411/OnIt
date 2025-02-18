package com.minho.ownit.auction;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.minho.ownit.member.Member;
import com.minho.ownit.resale.ResaleCategory;

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
@Entity(name = "auction")
public class Auction {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_seq")
    @SequenceGenerator(name = "auction_seq", sequenceName = "seq_auction_no", allocationSize = 1)
    @Column(name = "auction_no")
    private Integer no;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;
    
    @Column(name = "auction_title")
    private String title;
    
    @Column(name = "auction_desc")
    private String description;
    
    @Column(name = "auction_thumbnail")
    private String thumbnail;
    
    @Column(name = "auction_price")
    private Integer price;
    
    @CreationTimestamp
    @Column(name = "auction_create")
    private Date date;
    
    @Column(name = "delivery")
    private String delivery;
    
    @Column(name = "direct_deal")
    private String deal;
    
    @Column(name = "auction_status")
    private String status;
    
    @Column(name = "final_bidder_id")
    private String bidder;

}
