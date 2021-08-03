package com.tapfuma.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Service
@NoArgsConstructor
@AllArgsConstructor
public class Test {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	String name;
	boolean status;	
	String duration;
	LocalDate createdOn;
	@JsonManagedReference
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
	Set<TestQuestion> testQuestions = new HashSet<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
	Set<TestAnswer> testAnswers = new HashSet<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "test", cascade = CascadeType.ALL)
	Set<UserExamStatus> userExamStatus = new HashSet<>();
	
	@PrePersist
	void createdOn(){
		createdOn = LocalDate.now();
	}
}
