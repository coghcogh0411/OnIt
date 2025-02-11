package com.minho.ownit.community;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "post_like")
public class CommunityLike {
	@Id
	@Column(name = "post_like_no")
	private Integer no;
	@Column(name = "user_id")
	private Integer id;
	@Column(name = "post_no")
	private Integer pno;
}
