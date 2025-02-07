package com.minho.ownit.member;

import java.util.Date;
import java.util.List;

import com.minho.ownit.community.Community;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "users")
public class Member {
	@Id
	@Column(name = "user_id")
	private String id;
	
	@Column(name = "user_email")
	private String email;
	
	@Column(name = "user_pw")
	private String pw;
	
	@Column(name = "user_nickname")
	private String nickname;
	
	@Column(name = "user_name")
	private String name;
	
	@Column(name = "user_birthday")
	private Date birthday;
	
	@Column(name = "user_phone")
	private String phone;
	
	@Column(name = "user_photo")
	private String photo;
	
	@Column(name = "user_address")
	private String addr;
	
	@OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
	private List<Community> communityList;
}
