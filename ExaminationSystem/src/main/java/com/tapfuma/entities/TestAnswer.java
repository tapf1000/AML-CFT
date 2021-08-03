package com.tapfuma.entities;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
public class TestAnswer {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	Long id;
	String answer;
	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "test_id")
	Test test;
	@JsonBackReference
	@ManyToOne(optional=false)
	@JoinColumn(name="test_question_id")
	TestQuestion testQuestion;
}
