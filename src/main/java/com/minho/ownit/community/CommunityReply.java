package com.minho.ownit.community;

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
@Entity(name = "post_comment")
public class CommunityReply {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "post_comment_seq")
    @SequenceGenerator(name = "post_comment_seq", sequenceName = "seq_post_comment_no", allocationSize = 1)
	@Column(name = "comment_no")
	private Integer no;
	@ManyToOne
	@JoinColumn(name = "post_no")
	private Community pno;
	@ManyToOne
	@JoinColumn(name = "user_id")
	private Member writer;
	@Column(name = "comment_text")
	private String reply;
	@CreationTimestamp
	@Column(name = "comment_date")
	private Date date;
}
