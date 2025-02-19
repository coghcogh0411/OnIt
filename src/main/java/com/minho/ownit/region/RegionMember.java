package com.minho.ownit.region;

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
@Entity(name = "region_user")
public class RegionMember {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "region_user_seq")
	@SequenceGenerator(name = "region_user_seq", sequenceName = "seq_region_user_no", allocationSize = 1)
	@Column(name = "region_user_no")
	private Integer no;
	@ManyToOne
	@JoinColumn(name = "region_name")
	private Region region;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member user;
}
