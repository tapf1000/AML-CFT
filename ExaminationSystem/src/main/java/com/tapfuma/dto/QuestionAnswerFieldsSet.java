package com.tapfuma.dto;

import com.opencsv.bean.CsvBindByName;

import lombok.Data;

@Data
public class QuestionAnswerFieldsSet {
	
	
	@CsvBindByName
	String question;
	@CsvBindByName
	String answers;
	@CsvBindByName
	String correctAnswer;
}
