package com.tapfuma.entities;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class TestQuestion {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	String question;
	String correctAnswer;
	@JsonManagedReference
	@OneToMany(mappedBy="testQuestion", cascade = CascadeType.ALL)
	Set<TestAnswer> testAnswers = new HashSet<>();
	@JsonBackReference
	@ManyToOne 
	@JoinColumn(name="test_id")
	Test test;
	@OneToMany(mappedBy = "testQuestion", cascade = CascadeType.ALL)
	Set<UserExamStatus> userExamStatuses;
	
}
