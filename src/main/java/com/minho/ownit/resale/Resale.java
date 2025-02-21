package com.minho.ownit.resale;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

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
@Entity(name = "resale")
public class Resale {

    @Id
    @SequenceGenerator(name = "resale_seq", sequenceName = "seq_resale_no", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resale_seq")
    @Column(name = "resale_no")
    private Integer no;
    
    @ManyToOne
    @JoinColumn(name = "category_no")
    private ResaleCategory category;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;
    
    @Column(name = "resale_title")
    private String title;
    
    @Column(name = "resale_desc")
    private String description;
    
    @Column(name = "resale_thumbnail")
    private String thumbnail;
    
    @Column(name = "resale_price")
    private Integer price;
    
    @CreationTimestamp
    @Column(name = "resale_create")
    private Date date;
    
    @Column(name = "delivery")
    private String delivery;
    
    @Column(name = "direct_deal")
    private String deal;

}
