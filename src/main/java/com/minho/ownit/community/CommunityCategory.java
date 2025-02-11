package com.minho.ownit.community;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "community_category")
public class CommunityCategory {
    
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_community_category_no")
    @SequenceGenerator(name = "seq_community_category_no", sequenceName = "seq_community_category_no", allocationSize = 1)
    @Column(name = "category_no")
    private Integer categoryNo;
    @Column(name = "category_name")
    private String name;
    @OneToMany(mappedBy = "category")
    private List<Community> posts;  
}
