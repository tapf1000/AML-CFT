package com.tapfuma.entities;



import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class UserExamStatus {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="user_id")
	User user;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name="test_id")
	Test test;
	@ManyToOne
	@JoinColumn(name="test_question_id")
	TestQuestion testQuestion;
	boolean correctlyAnswered;
}
