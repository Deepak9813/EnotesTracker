package com.dpk.Enotes.service;

import java.util.List;

import com.dpk.Enotes.model.Note;
import com.dpk.Enotes.model.User;

public interface UserService {

	User userSignup(User user, String url);

	// User saveUser(User user, String url);, User addUser(User user, String url); j lekhda ni hunxa method
	// name

	User getUserByEmail(String email);

	void removeSessionMessage();

//	========== For Forgot Password ================
	// User getUserByEmail(String email); //already mathi banaya raixu
	// User getUserByEmailAndPhone(Sring email, String phone); // yesari garda ni
	// hunxa
	User getUserById(int id);

	User updateUser(User user);

	// for currentPassword[database] and currentPassword[form]
	boolean checkPassword(String email, String currentPassword);

	// ===================== update User Profile ====================
	User updateUserProfile(User user);

	
	//======================= User Account Verification By Email ==============================
	void sendEmail(User user, String url);
	
	boolean verifyAccount(String verificationCode);
	
}
