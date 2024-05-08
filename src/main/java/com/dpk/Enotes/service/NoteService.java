package com.dpk.Enotes.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.dpk.Enotes.model.Note;
import com.dpk.Enotes.model.User;

public interface NoteService {

	Note addNote(Note note); // Note saveNote(Note note) lekda ni hunxa

	boolean deleteNote(int id);

	Note updateNote(Note note);

	Note getNoteById(int id);

	// jun user login xa usko matra notes chahiyo(nikalna), euta user ko multiple notes huna sakxa[i.e euta user le multiple notes save garna sakxa] tesaile List use gareko method ko return type
//	List<Note> getNotesByUser(User user);
	
	//=== [for Pagination] mathi ko List<Note> getNotesByUser(User user); comment garera List = Note lekhne
	Page<Note> getNotesByUser(User user, int pageNo);
	
	// ==================== Global Notes ====================
//	List<Note> getAllNotes();
	
	//=== [for Pagination] mathi ko List<Note> getNotesByUser(User user); comment garera List = Note lekhne
	Page<Note> getAllNotes(int pageNo);
		
	
}
