package com.dpk.Enotes.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dpk.Enotes.model.Note;
import com.dpk.Enotes.model.User;

@Repository				//this notation is optional
public interface NoteRepository extends JpaRepository<Note, Integer>{

	//List<Note> findByUser(User user);

	Page<Note> findByUser(User user, Pageable pageable);
	

}
