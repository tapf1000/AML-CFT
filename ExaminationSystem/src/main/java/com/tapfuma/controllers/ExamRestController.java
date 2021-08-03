package com.tapfuma.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tapfuma.dto.ExamUserDetails;
import com.tapfuma.dto.JSONResponse;
import com.tapfuma.entities.UserExamStatus;
import com.tapfuma.entities.Test;
import com.tapfuma.entities.TestAnswer;
import com.tapfuma.entities.TestQuestion;
import com.tapfuma.entities.User;
import com.tapfuma.repositories.UserExamStatusRepository;
import com.tapfuma.repositories.TestAnswersRepository;
import com.tapfuma.repositories.TestQuestionsRepository;
import com.tapfuma.repositories.TestRepository;
import com.tapfuma.repositories.UserRepository;


@CrossOrigin("*")
@RestController
public class ExamRestController {
	@Autowired
	TestQuestionsRepository testQuestionRepository;
	@Autowired
	TestAnswersRepository testAnswerRepository;
	@Autowired
	TestRepository testRepository;
	@Autowired
	UserExamStatusRepository examStatusRepository;
	@Autowired
	UserRepository userRepository;	
	
	@GetMapping("/answer")
	public ResponseEntity<JSONResponse> retrieveNextQuestion(@RequestParam(name="questionNumber", defaultValue = "0") Long questionNum,
			@RequestParam("answer") String answer,
			@AuthenticationPrincipal ExamUserDetails examUserDetails) {
		User user = userRepository.findByEmail(examUserDetails.getUsername());
		JSONResponse jsonResponse = new JSONResponse();
		Test test = testRepository.findFirstByStatusOrderByIdAsc(true);
		Optional<TestQuestion> testQuestionCurrentOptional = testQuestionRepository.findByIdAndTest(questionNum, test);
		if(testQuestionCurrentOptional.isPresent()) {
			TestQuestion testQuestionCurrent  = testQuestionCurrentOptional.get();
			UserExamStatus examStatus = new UserExamStatus();
			if(testQuestionCurrent.getCorrectAnswer().equalsIgnoreCase(answer)) 
				examStatus.setCorrectlyAnswered(true);
			else
				examStatus.setCorrectlyAnswered(false);
			examStatus.setTestQuestion(testQuestionCurrent);
			examStatus.setUser(user);
			examStatus.setTest(test);
			examStatusRepository.save(examStatus);
			TestQuestion testQuestion = testQuestionRepository.findByIdAndTest(questionNum+1, test).get();
			List<TestAnswer> testAnswers = testAnswerRepository.findAllByTestQuestion(testQuestion);
			jsonResponse.setQuestion(testQuestion.getQuestion());
			jsonResponse.setQuestionNumber(testQuestion.getId());
			jsonResponse.setAnswer1(testAnswers.get(0).getAnswer());
			jsonResponse.setAnswer2(testAnswers.get(1).getAnswer());
			jsonResponse.setAnswer3(testAnswers.get(2).getAnswer());
			jsonResponse.setAnswer4(testAnswers.get(3).getAnswer());
			jsonResponse.setStatus("ACCEPTED");
			return ResponseEntity.status(HttpStatus.ACCEPTED).body(jsonResponse);
		}else if(testQuestionRepository.count()+1 == questionNum+1){
			jsonResponse.setQuestion("");
			jsonResponse.setQuestionNumber(0L);
			jsonResponse.setAnswer1("");
			jsonResponse.setAnswer2("");
			jsonResponse.setAnswer3("");
			jsonResponse.setAnswer4("");
			jsonResponse.setStatus("NO_CONTENT");
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(jsonResponse);			
		}else {
			jsonResponse.setQuestion("");
			jsonResponse.setQuestionNumber(0L);
			jsonResponse.setAnswer1("");
			jsonResponse.setAnswer2("");
			jsonResponse.setAnswer3("");
			jsonResponse.setAnswer4("");
			jsonResponse.setStatus("NOT_FOUND");
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(jsonResponse);
		}		
	}
}
