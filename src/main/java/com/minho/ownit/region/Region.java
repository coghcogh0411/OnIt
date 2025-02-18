package com.minho.ownit.region;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity(name = "region")
public class Region {
	@Id
	@Column(name = "region_name")
	private String name;
	@Column(name = "region_1depth_name")
	private String firstName;
	@Column(name = "region_2depth_name")
	private String secondName;
}
