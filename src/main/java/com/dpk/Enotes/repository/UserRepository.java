package com.dpk.Enotes.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.dpk.Enotes.model.User;

@Repository		//this notation is also optional
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);

	//====== for Password Change[currentPassword in database]============
	@Query("SELECT u.password FROM User u WHERE u.email = ?1")
    String findPasswordByEmail(String email);

	//============== User Account Verification By Email ===============
	User findByVerificationCode(String verificationCode);
	

	//=========== Limit Login Attempt ================
	
}
