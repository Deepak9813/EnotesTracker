package com.dpk.Enotes.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "note_tbl")
public class Note {
	
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // AI[auto]
	private int id;
	private String title;
	
	@Column(columnDefinition = "TEXT")				 
	private String description; // description = content
	
	//yo @Column(columnDefinition = "TEXT") nalekhe database ma varchar(255) wala datatype create garxa but yo lekhe database ma TEXT datatype create garxa ani hami large data store garna sakxau 

	@DateTimeFormat(iso = ISO.DATE)			// YYYY-MM-DD
	private LocalDate date;

	// YYYY-MM-DD ma hunxa but yo @DateTimeFormat(iso = ISO.DATE) notation nalekda ni yehi nai aauxa LocalDate datatype use garye

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user; // fk = foreign key in notes_tbl
	
	
	

}
