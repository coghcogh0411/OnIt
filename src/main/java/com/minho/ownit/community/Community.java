package com.minho.ownit.community;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;

import com.minho.ownit.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "post")
public class Community {
	@Id
	@Column(name = "post_no")
	private Integer no;
	@Column(name = "category_no")
	private Integer category;
	@Column(name = "post_title")
	private String title;
	@Column(name = "post_content")
	private String content;
	@CreationTimestamp
	@Column(name = "post_reg")
	private Date date;
	@Column(name = "post_view")
	private Integer view;
	@Column(name = "post_like")
	private Integer like;
	@Column(name = "post_comment")
	private Integer comment;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member id;
	
}
