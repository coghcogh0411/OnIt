package com.minho.ownit.member;

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
@Entity(name = "follow")
public class Follow {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_follow_no")
	@SequenceGenerator(name = "seq_follow_no", sequenceName = "seq_follow_no", allocationSize = 1)
	@Column(name = "follow_no")
	private Integer followNo;
	@ManyToOne
	@JoinColumn(name = "follower_id")
	private Member follower;
	@ManyToOne
	@JoinColumn(name = "following_id")
	private Member following;
}
