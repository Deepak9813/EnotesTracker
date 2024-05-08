package com.dpk.Enotes.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dpk.Enotes.model.User;
import com.dpk.Enotes.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;

@Log
@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping("/")
	public String getIndex() {

		return "index";
	}

	@GetMapping("/signup")
	public String getSignup() {

		return "Signup";
	}

	@PostMapping("/signup")
	public String postSignup(@ModelAttribute User user, HttpSession session, HttpServletRequest request) {

		String url = request.getRequestURL().toString();
		System.out.println(url); // http:localhost:8080/signup

		url = url.replace(request.getServletPath(), "");
		System.out.println(url); // http:localhost:8080

		// Check user(email) already exist or not
		User u = userService.getUserByEmail(user.getEmail());
		if (u != null) {
			

			session.setAttribute("msg", "User(Email) Already Exist..!!"); // ("key", value)
			return "redirect:/signup";

		}

		User usr = userService.userSignup(user, url);
		if (usr != null) {
			
			log.info("================== Signup Success ======================");

			session.setAttribute("msg", "Save Successfully"); // ("key", value)
			// return "redirect:/signup";

		} else {
			
			log.info("================== Signup Failed ======================");
			
			session.setAttribute("msg", "Something wrong on server"); // ("key", value)
			// return "redirect:/signup";
		}

		return "redirect:/signup";
	}

	@GetMapping("/verify")
	public String verifyAccount(@RequestParam String code, Model model) {

		boolean f = userService.verifyAccount(code);

		if (f) { // true

			model.addAttribute("msg", "Successfully your account is verified");
			// return "Message";
		} else {

			model.addAttribute("msg", "May be your verification code is incorrect or already verified");
			// return "Message";
		}

		return "Message";
	}

	@GetMapping("/signin")
	public String getLogin() {

		return "Login";
	}

	@ModelAttribute
	public void commonUser(Principal p, Model model) {

		if (p != null) {

			String email = p.getName(); // j bata login xa tyo aauxa

			User usr = userService.getUserByEmail(email);

			model.addAttribute("user", usr); // ("key", value)
		}
	}

	// =================== for Forgot Password =========================

	@GetMapping("/forgotPassword")
	public String getForgotPasswordForm() {

		return "ForgotPasswordForm";
	}

	@PostMapping("/forgotPassword")
	public String postForgotPasswordForm(@ModelAttribute User user, HttpSession session) {

		User usr = userService.getUserByEmail(user.getEmail());

		if (usr != null) {

			// return "ChangePasswordForm";

			// ============= Password Change garda id lagera(id controller ma lagera) jaane,
			// ani uta receive garne========
			return "redirect:/changePassword/" + usr.getId();
		}

		session.setAttribute("msg", "Invalid Email..!!"); // ("key", value)
		return "redirect:/forgotPassword";

	}

	// id receive garne ani usko(User ko) data lagera ChangePasswordForm ma jaane
	// [i.e page open huda data aaunu paryo]
	@GetMapping("/changePassword/{id}")
	public String getChangePasswordForm(@PathVariable int id, Model model) {

		User user = userService.getUserById(id);

		model.addAttribute("userModel", user); // ("key", value)
		// Here, userModel = userObject (j) lekhda ni hunxa because User ko object aauxa

		// model.addAttribute("userModel", userService.getUserById(id)); //("key",
		// value)

		return "ChangePasswordForm";
	}

	@PostMapping("/changePassword")
	public String postChangePasswordForm(@ModelAttribute User user, HttpSession session) {

		User usr = userService.updateUser(user);
		if (usr != null) {
			
			log.info("================== Password Change Success ======================");

			session.setAttribute("msg", "Password changed successfully"); // ("key",value)
			// return "redirect:/signin";
		} else {

			log.info("================== Password Change Failed ======================");
			
			session.setAttribute("msg", "Something wrong on server"); // ("key",value)
			// return "redirect:/signin";
		}

		return "redirect:/signin";
	}

	// id receive garne view baata controller ma send gareko xu, ani usko(User ko)
	// data lagera ChangePasswordFormInLoginMode ma jaane
	// [i.e page open huda data aaunu paryo]
	@GetMapping("/user/changePasswordInLoginMode/{id}")
	public String getChangePasswordFormInLoginMode(@PathVariable int id, Model model) {

		User user = userService.getUserById(id);

		model.addAttribute("userModel", user); // ("key", value)
		// Here, userModel = userObject (j) lekhda ni hunxa because User ko object aauxa

		// model.addAttribute("userModel", userService.getUserById(id)); //("key",
		// value)

		return "ChangePasswordFormInLoginMode";
	}

	@PostMapping("/user/changePasswordInLoginMode")
	public String postChangePasswordFormInLoginMode(@ModelAttribute User user, @RequestParam String currentPassword,
			@RequestParam String newPassword, Principal p, HttpSession session) {

		// Here we need to check current database password and currentPassword from input field match or not
		if (p != null) {

			String email = p.getName(); // j bata login garyo tyo aauxa

			// yo email baata user find garne
			User u = userService.getUserByEmail(email);

			String dbPassword = u.getPassword();
			
			boolean passwordMatches = passwordEncoder.matches(currentPassword, dbPassword);
			
			if(!passwordMatches) {
				
				session.setAttribute("msg", "Current password doesn't match");
				//return "redirect:/user/changePasswordInLoginMode/" + u.getId();
				return "redirect:/user/changePasswordInLoginMode/" + user.getId();
			}
			
		}
			
		user.setPassword(newPassword);

		User usr = userService.updateUser(user);
		if (usr != null) {
			
			log.info("================== Password Change Success in Login Mode ======================");

			session.setAttribute("msg", "Password changed successfully"); // ("key",value)
			// return "redirect:/signin";
		} else {

			log.info("================== Password Change Failed in Login Mode ======================");
			
			session.setAttribute("msg", "Something wrong on server"); // ("key",value)
			// return "redirect:/signin";
		}

		return "redirect:/signin";
	}

}
