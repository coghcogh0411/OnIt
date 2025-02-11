package com.minho.ownit.community;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;

import com.minho.ownit.member.Member;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "post")
public class Community {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_seq")
	@SequenceGenerator(name = "post_seq", sequenceName = "seq_post_no", allocationSize = 1)
	@Column(name = "post_no")
	private Integer no;
	@ManyToOne
	@JoinColumn(name = "category_no")
	private CommunityCategory category;
	@Column(name = "post_title")
	private String title;
	@Column(name = "post_content")
	private String content;
	@CreationTimestamp
	@Column(name = "post_reg")
	private Date date;
	@Column(name = "post_view")
	private Integer view;
	@Column(name = "post_comment")
	private Integer comment;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member writer;

	@OneToMany(mappedBy = "pno", cascade = CascadeType.ALL)
	private List<CommunityReply> replies;
}
