package com.dpk.Enotes.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dpk.Enotes.model.Note;
import com.dpk.Enotes.model.User;
import com.dpk.Enotes.repository.NoteRepository;
import com.dpk.Enotes.service.NoteService;

@Service
public class NoteServiceImpl implements NoteService {

	@Autowired
	private NoteRepository noteRepo;

	@Override
	public Note addNote(Note note) {

		// return noteRepo.save(note);

		Note newNote = noteRepo.save(note);
		return newNote;
	}

	@Override
	public boolean deleteNote(int id) {

		Note note = noteRepo.findById(id).get();
		if (note != null) {

			noteRepo.delete(note);
			// noteRepo.deleteById(id);
			return true;
		}

		return false;
	}

	@Override
	public Note updateNote(Note note) {

		// return noteRepo.save(note);
		Note newNote = noteRepo.save(note);
		return newNote;
	}

	@Override
	public Note getNoteById(int id) {
		
		return noteRepo.findById(id).get();
	}

	/*
	@Override
	public List<Note> getNotesByUser(User user) {
		
		return noteRepo.findByUser(user);
	}
	
	*/
	
	@Override
	public Page<Note> getNotesByUser(User user, int pageNo) {
		
		 Pageable pageable =PageRequest.of(pageNo, 5);		//euta page ma jati dekhaune man xa teti rakhne(eg. 5)		
		
		
		return noteRepo.findByUser(user, pageable);
	}


/*
	@Override
	public List<Note> getAllNotes() {
		
		return noteRepo.findAll();
	}
*/
	
	@Override
	public Page<Note> getAllNotes(int pageNo) {
		
		Pageable pageable=PageRequest.of(pageNo, 5);	//euta page ma jati dekhaune man xa teti rakhne(eg. 5)
		
		return noteRepo.findAll(pageable);
	}
	

}
