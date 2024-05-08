package com.dpk.Enotes.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "user_tbl")
public class User {

	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AI[auto]
	private int id;

	@Column(nullable = false)		//optional
	private String fullName;
	
	@Column(nullable = false)		//optional
	private String profession;

	@Column(nullable = false, unique = true)		//optional
	private String email;

	@Column(nullable = false)		//optional
	private String address;

	@Column(nullable = false)		//optional
	private String gender;

	@Column(nullable = false)		//optional
	private String password;
	
	
	private String role;
	

	// Note: front end baata input ma required rakhye null aaudaina and unique backend code bata  garna  yo @Column(nullable = false, unique = true), @Column(nullable = false) nalekhda ni huxa

	
	//======================= User Account Verification By Email ==============================
	private boolean enable;
	private String verificationCode;
	

}
