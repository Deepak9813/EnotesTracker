package com.dpk.Enotes.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dpk.Enotes.model.Note;
import com.dpk.Enotes.model.User;
import com.dpk.Enotes.service.NoteService;
import com.dpk.Enotes.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.extern.java.Log;

@Log
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private NoteService noteService;

	@GetMapping("/addNotes")
	public String getAddNotes() {

		return "AddNotes";
	}

	@PostMapping("/addNotes")
	public String addNotes(@ModelAttribute Note note, HttpSession session, Principal p, Model model) {

		// ========= Note save garnu vanda agaadi, Note kun date ma save hudai xa,
		// teslai(date) lai save garne ===========
		note.setDate(LocalDate.now());

		// ======= Yo Note kun User save gardai xa, tyo(User) ni set garne[i.e. jun user
		// login xa usle nai save garxa] ===========
		// note.setUser(commonUser(p, model));
		User user = commonUser(p, model);
		note.setUser(user);

		Note n = noteService.addNote(note);
		if (n != null) {

			log.info("================== Note Save Success ======================");
			
			session.setAttribute("msg", "Note saved successfully"); // ("key", value)
			// return "redirect:/user/addNotes";
		} else {
			
			log.info("================== Note Save Failed ======================");

			session.setAttribute("msg", "Something wrong on server"); // ("key", value)
			// return "redirect:/user/addNotes";
		}

		return "redirect:/user/addNotes";
	}

	
	/*
	@GetMapping("/viewNotes")
	public String getViewNotes(Principal p, Model model) {

		// User user = commonUser(p, model);
		//List<Note> noteList= noteService.getNotesByUser(user);

		List<Note> noteList = noteService.getNotesByUser(commonUser(p, model));
		model.addAttribute("nList", noteList); // ("key", value)

		// model.addAttribute("nList", noteService.getNotesByUser(commonUser(p,
		// model))); //("key", value)

		return "ViewNotes";
	}
	
	*/
	
	//======= For Pagination [change List => Page ]======================
	@GetMapping("/viewNotes")
	public String getViewNotes(Principal p, Model model, @RequestParam(defaultValue = "0") int pageNo) {

		User user = commonUser(p, model);
		
		Page<Note> noteList = noteService.getNotesByUser(user, pageNo);
		
		//model.addAttribute("nList", notesList); //page use garye .getContent() ni garnu parxa, so
		
		model.addAttribute("currentPage", pageNo);			//("key", value)
		model.addAttribute("totalElements", noteList.getTotalElements());
		model.addAttribute("totalPages", noteList.getTotalPages());
		
		model.addAttribute("nList", noteList.getContent());		
		
		return "ViewNotes";
	}
	

	@GetMapping("/deleteNote/{id}")
	public String deleteNote(@PathVariable int id, HttpSession session) {

		boolean f = noteService.deleteNote(id);

		if (f) { // if true
			
			log.info("================== Note Delete Success ======================");

			session.setAttribute("msg", "Note deleted successfully"); // ("key", value)
			// return "redirect:/user/viewNotes";
		} else {

			log.info("================== Note Delete Failed ======================");
			
			session.setAttribute("msg", "Something wrong on server"); // ("key", value)
			// return "redirect:/user/viewNotes";
		}

		return "redirect:/user/viewNotes";
	}

	@GetMapping("/editNote")
	public String getEditNotes(@RequestParam int id, Model model) {

		Note note = noteService.getNoteById(id);
		model.addAttribute("noteModel", note); // ("key", value)
		// here, noteModel = noteObject (j) lekhda ni hunxa because Note ko object aauxa

		// model.addAttribute("noteModel", noteService.getNoteById(id));

		return "EditNotes";
	}

	@PostMapping("/updateNote")
	public String updateNotes(@ModelAttribute Note note, HttpSession session, Principal p, Model model) {

		// ========= Note save(update) garnu vanda agaadi, Note kun date ma save hudai
		// xa, teslai(date) lai save(update) garne ===========
		note.setDate(LocalDate.now());

		// ======= Yo Note kun User save(update) gardai xa, tyo(User) ni set garne[i.e.
		// jun user login xa usle nai save(update) garxa] ===========
		// note.setUser(commonUser(p, model));
		User user = commonUser(p, model);
		note.setUser(user);

		Note n = noteService.updateNote(note);
		if (n != null) {
			
			log.info("================== Note Update Success ======================");

			session.setAttribute("msg", "Note update successfully"); // ("key", value)
			// return "redirect:/user/viewNotes";
		} else {

			log.info("================== Note Update Failed ======================");
			
			session.setAttribute("msg", "Something wrong on server"); // ("key", value)
			// return "redirect:/user/viewNotes";
		}

		return "redirect:/user/viewNotes";
	}

	/*
	 * @ModelAttribute public void commonUser(Principal p, Model model) {
	 * 
	 * if (p != null) {
	 * 
	 * String email = p.getName(); // j bata login xa tyo aauxa
	 * 
	 * User usr = userService.getUserByEmail(email);
	 * 
	 * model.addAttribute("user", usr); //("key", value) } }
	 */

	@ModelAttribute
	public User commonUser(Principal p, Model model) {

		if (p != null) {

			String email = p.getName(); // j bata login xa tyo aauxa

			User usr = userService.getUserByEmail(email);

			model.addAttribute("user", usr); // ("key", value)

			return usr;
		}

		return null;
	}

	/*
	 * //Yeha if nalekhda ni huxa, because p = null(user =null) audaina , hami login
	 * mode ma xau[login vaye null aaudaina]
	 * 
	 * @ModelAttribute public User commonUser(Principal p, Model model) {
	 * 
	 * String email = p.getName(); // j bata login xa tyo aauxa
	 * 
	 * User usr = userService.getUserByEmail(email);
	 * 
	 * model.addAttribute("user", usr); //("key", value)
	 * 
	 * return usr;
	 * 
	 * }
	 */

	// yo page ma @ModelAttribute bata model ma user key lageko xau(i.e user lageko)
	// xau tyo receive garne
	@GetMapping("/profile")
	public String getUserProfile() {

		return "UserProfile";
	}

	// Next Way[using HttpSession session]
	@GetMapping("/profile1")
	public String getUserProfile1(Principal p, Model model, HttpSession session) {

		User u = commonUser(p, model);

		session.setAttribute("user", u); // ("key", value)

		return "UserProfile1";
	}

	// Next Way[using Form ]
	@GetMapping("/profile2")
	public String getUserProfile2(Principal p, Model model) {

		User u = commonUser(p, model);
		model.addAttribute("userModel", u); // ("key", value);

		//======== OR ============
		//User u = commonUser(p, model);
		// User user = userService.getUserById(u.getId());
		// model.addAttribute("userModel", user); //("key", value);

		return "UserProfile2";
	}

	@GetMapping("/editProfile")
	public String getEditUserProfile(@RequestParam int id, Model model) {
		
		User user = userService.getUserById(id);
		model.addAttribute("userModel", user);   	//("key", value)
		//here, userModel = userObject (j) lekhda ni hunxa because User ko object aauxa
		
		//model.addAttribute("userModel", userService.getUserById(id)); 	//("key", value)
	
		return "EditUserProfile";
	}
	
	@PostMapping("/updateProfile")
	public String updateUserProfile(@ModelAttribute User user, HttpSession session) {
		
		
		User usr = userService.updateUserProfile(user);
		if(usr != null) {
			
			log.info("================== User Profile Update Success ======================");
			
			session.setAttribute("msg", "Profile update successfully");		//("key", value)
			//return "redirect:/user/profile";      
		}
		else {
			
			log.info("================== User Profile Update Failed ======================");
			
			session.setAttribute("msg", "Something wrong on server");		//("key", value)
			//return "redirect:/user/profile";    
		}
		
		return "redirect:/user/profile"; 
	}
	
	
	// ================== Global Notes ==================
	/*
	@GetMapping("/viewGlobalNotes")
	public String getViewGlobalNotes(Model model) {	

		List<Note> noteList = noteService.getAllNotes();
		model.addAttribute("nList", noteList); // ("key", value)

		// model.addAttribute("nList", noteService.getAllNotes()); //("key", value)

		return "ViewGlobalNotes";
	}
*/
	//======= For Pagination in ViewGlobalNotes page [change List => Page ]======================
	@GetMapping("/viewGlobalNotes")
	public String getViewGlobalNotes(Principal p, Model model, @RequestParam(defaultValue = "0") int pageNo) {

		User user = commonUser(p, model);
		
		Page<Note> noteList = noteService.getAllNotes(pageNo);
		
		//model.addAttribute("nList", notesList); //page use garye .getContent() ni garnu parxa, so
		
		model.addAttribute("currentPage", pageNo);			//("key", value)
		model.addAttribute("totalElements", noteList.getTotalElements());
		model.addAttribute("totalPages", noteList.getTotalPages());
		
		model.addAttribute("nList", noteList.getContent());		
		
		return "ViewGlobalNotes";
	}
	
	
}
