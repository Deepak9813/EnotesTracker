package com.dpk.Enotes.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.dpk.Enotes.model.User;
import com.dpk.Enotes.repository.UserRepository;
import com.dpk.Enotes.service.UserService;

import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JavaMailSender javaMailSender;

	@Override
	public User userSignup(User user, String url) {

		// ============== Password Encrypt ===============
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);

		// user.setPassword(passwordEncoder.encode(user.getPassword()));

		// ============= Set Role ==============
		user.setRole("ROLE_USER");
		
		// ====== first account create garda enable lai false rakhne(save garne) because account verified gareko xaina And verificationCode lai random save garne =====
		user.setEnable(false);
		user.setVerificationCode(UUID.randomUUID().toString());
		

		// return userRepo.save(user);
		User newUser = userRepo.save(user);
		if(newUser != null) {
			
			sendEmail(newUser, url);
		}
		return newUser;
	}

	@Override
	public User getUserByEmail(String email) {

		return userRepo.findByEmail(email);
	}

	@Override
	public void removeSessionMessage() {

		HttpSession session = ((ServletRequestAttributes) (RequestContextHolder.getRequestAttributes())).getRequest()
				.getSession();

		session.removeAttribute("msg"); // ("key", value) ma key lekhne

	}

	@Override
	public User getUserById(int id) {

		return userRepo.findById(id).get();
	}

	@Override
	public User updateUser(User user) {

		// ============== Password Encrypt ===============
		String password = passwordEncoder.encode(user.getPassword());
		user.setPassword(password);

		// user.setPassword(passwordEncoder.encode(user.getPassword()));

		// ============= Set Role ==============
		user.setRole("ROLE_USER");
		
		//account verifed vaye paxi rakhnxau ni tei value set garne
		user.setEnable(true);
		user.setVerificationCode(null);

		// return userRepo.save(user);
		User newUser = userRepo.save(user);
		return newUser;
	}

	@Override
	public boolean checkPassword(String email, String currentPassword) {

		String encodedPassword = userRepo.findPasswordByEmail(email);
		return passwordEncoder.matches(currentPassword, encodedPassword);
	}

	@Override
	public User updateUserProfile(User user) {

		// ============= Set Role ==============
		user.setRole("ROLE_USER");

		// return userRepo.save(user);
		User newUser = userRepo.save(user);
		return newUser;
	}

	//======================= User Account Verification By Email ==============================

	@Override
	public void sendEmail(User user, String url) {

		String from = "baijdeepak5@gmail.com";
		String to = user.getEmail();
		String subject = "Account Verification";
		String content = "Dear [[name]],<br>" + "Please click the link below to verify your registration:" + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>" + "Thank you,<br>" +"DB Company";
		
		try {
			
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message);
			
			helper.setFrom(from, "DB Company");
			helper.setTo(to);
			helper.setSubject(subject);
			
			content = content.replace("[[name]]", user.getFullName());
			
			String siteUrl = url + "/verify?code=" + user.getVerificationCode();
			
			content = content.replace("[[URL]]", siteUrl);
			
			helper.setText(content, true);
			
			javaMailSender.send(message);
			
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
	}

	@Override
	public boolean verifyAccount(String verificationCode) {
		
		User user = userRepo.findByVerificationCode(verificationCode);
		
		if(user == null) {
			
			return false;
		}
		
		else {
			
			user.setEnable(true);
			user.setVerificationCode(null);
			
			userRepo.save(user);
			
			return true;
		}		
	}

}
