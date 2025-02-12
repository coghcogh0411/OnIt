package com.minho.ownit.community;

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
@Entity(name = "post_like")
public class CommunityLike {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_like_no")
	@SequenceGenerator(name = "seq_post_like_no", sequenceName = "seq_post_like_no", allocationSize = 1)
	@Column(name = "post_like_no")
	private Integer no;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member user;

	@ManyToOne
	@JoinColumn(name = "post_no")
	private Community post; 
}
