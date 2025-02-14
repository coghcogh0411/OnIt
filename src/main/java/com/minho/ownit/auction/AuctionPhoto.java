package com.minho.ownit.auction;

import jakarta.persistence.CascadeType;
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
@Entity(name = "auction_photo")
public class AuctionPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "auction_photo_seq")
    @SequenceGenerator(name = "auction_photo_seq", sequenceName = "seq_auction_photo_no", allocationSize = 1)
    @Column(name = "auction_photo_no")
    private Integer photoId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auction_no")
    private Auction auction;

    @Column(name = "auction_photo_url")
    private String url;

}
