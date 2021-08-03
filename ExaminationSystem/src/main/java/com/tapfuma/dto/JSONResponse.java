package com.tapfuma.dto;

import lombok.Data;

@Data
public class JSONResponse {
	
	String question;
	String answer1;
	String answer2;
	String answer3;
	String answer4;
	Long questionNumber;
	String status;
}
