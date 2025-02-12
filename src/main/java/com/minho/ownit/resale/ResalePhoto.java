package com.minho.ownit.resale;

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
@Entity(name = "resale_photo")
public class ResalePhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "resale_photo_seq")
    @SequenceGenerator(name = "resale_photo_seq", sequenceName = "seq_resale_photo_no", allocationSize = 1)
    @Column(name = "resale_photo_no")
    private Integer photoId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "resale_no")
    private Resale resale;

    @Column(name = "resale_photo_url")
    private String url;

}
