package com.tapfuma.controllers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.websocket.server.PathParam;

import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.expression.Arrays;

import com.itextpdf.styledxmlparser.css.media.MediaType;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.tapfuma.dto.ExamUserDetails;
import com.tapfuma.dto.QuestionAnswerFieldsSet;
import com.tapfuma.dto.UserScore;
import com.tapfuma.entities.Test;
import com.tapfuma.entities.TestAnswer;
import com.tapfuma.entities.TestQuestion;
import com.tapfuma.entities.User;
import com.tapfuma.entities.UserExamStatus;
import com.tapfuma.repositories.TestAnswersRepository;
import com.tapfuma.repositories.TestQuestionsRepository;
import com.tapfuma.repositories.TestRepository;
import com.tapfuma.repositories.UserExamStatusRepository;
import com.tapfuma.repositories.UserRepository;
import com.tapfuma.services.PDFGeneratorService;

@Controller
public class AdminController {
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	TestAnswersRepository testAnswersRepository;
	@Autowired
	TestQuestionsRepository testQuestionsRepository;
	@Autowired
	TestRepository testRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	UserExamStatusRepository examStatusRepository;
	
	@GetMapping("/admin")
    public String admin() {
        return "admin";
    }
	@GetMapping("/login")
    public String login() {
        return "login";
    }
	
	@PostMapping("/login")
    public String loginHandler(Model model, @AuthenticationPrincipal ExamUserDetails examUserDetails) {
		model.addAttribute("userName", examUserDetails.getUsername());
        return "user";
    }
	
	@GetMapping("/register")
	public String registerPaged() {
		return "register";
	}
	
	@PostMapping("/register")
	public String register(Model model,
			@RequestParam("username") String username,
			@RequestParam("name") String name,
			@RequestParam("surname") String surname,
			@RequestParam("password") String password
			) {
		if(userRepository.findByEmail(username) != null) {
			model.addAttribute("message", username.toUpperCase()+" ALREADY EXISTS !!!");
			return "register";
		}else {
			User user = new User();
			user.setEmail(username);
			user.setName(name);
			user.setSurname(surname);
			user.setPassword(passwordEncoder.encode(password));
			user.setEnabled(true);
			user.setCredentialNonExpired(true);
			user.setAccountNonLocked(true);
			user.setAccountNonExpired(true);
			userRepository.save(user);
			return "login";
		}		
	}
	
	@GetMapping("/")	
    public String index() {
			return "login";
    }

    @PostMapping("/upload-csv-file")
    public String uploadCSVFile(@RequestParam("file") MultipartFile file, 
    		@RequestParam("name") String name, 
    		@RequestParam("duration") double duration, 
    		Model model) {

        // validate file
        if (file.isEmpty()) {
            model.addAttribute("message", "Please select a CSV file to upload.");
            model.addAttribute("status", false);
        } else {
        	
        	int hours = (int) (duration);
        	int minutes = (int)  (60*(duration-hours));
        	int seconds = 0;
        	String newDuration = hours+":"+minutes+":"+seconds;
        	Test test = new Test();
        	test.setName(name);
        	test.setStatus(false);
        	test.setDuration(newDuration);
        	
            // parse CSV file to create a list of `QuestionAnswerFieldsSet` objects
            try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

                // create csv bean reader
                CsvToBean<QuestionAnswerFieldsSet> csvToBean = new CsvToBeanBuilder<QuestionAnswerFieldsSet>(reader)
                        .withType(QuestionAnswerFieldsSet.class)
                        .withIgnoreLeadingWhiteSpace(true)
                        .build();

                // convert `CsvToBean` object to list of QuestionAnswerFieldsSet
                List<QuestionAnswerFieldsSet> questionAnswerFieldsSets = csvToBean.parse();
                Set<TestQuestion> testQuestions = new HashSet<>();
                for(QuestionAnswerFieldsSet questionAnswerFieldsSet : questionAnswerFieldsSets) {
                	TestQuestion testQuestion = new TestQuestion();
                	testQuestion.setQuestion(questionAnswerFieldsSet.getQuestion());
                	testQuestion.setCorrectAnswer(questionAnswerFieldsSet.getCorrectAnswer());
                	testQuestion.setTest(test);
                	String[] possibleAnswers = questionAnswerFieldsSet.getAnswers().trim().split("\\|");
                	Set<TestAnswer> testAnswers = new HashSet<>();
                	for(String possibleAnswer : possibleAnswers) {
                		TestAnswer testAnswer = new TestAnswer();
                		testAnswer.setAnswer(possibleAnswer);
                		testAnswer.setTest(test);
                		testAnswer.setTestQuestion(testQuestion);
                		testAnswers.add(testAnswer);
                	}
                	testQuestion.setTestAnswers(testAnswers);
                	testQuestions.add(testQuestion);
                	test.setTestAnswers(testAnswers);
                }
                test.setTestQuestions(testQuestions);
                testRepository.save(test);

                // save QuestionAnswerFieldsSet list on model
            	model.addAttribute("message", "Upload Successful");
                model.addAttribute("questionAnswerFieldsSets", questionAnswerFieldsSets);
                model.addAttribute("status", true);
            } catch (Exception ex) {
            	System.out.println(ex.getMessage());
                model.addAttribute("message", "An error occurred while processing the CSV file.");
                model.addAttribute("status", false);
            }
        }
        return "admin";
    }
    
    @GetMapping("/test")
    public String userTestPageInit(Model model, @AuthenticationPrincipal ExamUserDetails examUserDetails) {
		model.addAttribute("userName", examUserDetails.getUsername());
    	return "user";
    }
    
    @PostMapping("/next-question")
    public String retrieveNextQuestion(Model model,
    		@RequestParam(name="questionID", defaultValue = "0") Long questionID,
    		@RequestParam("answer") String answer,
    		@RequestParam("count") int count,
    		@RequestParam("timer") String timer,
			@AuthenticationPrincipal ExamUserDetails examUserDetails) {
		User user = userRepository.findByEmail(examUserDetails.getUsername());
		Test test = testRepository.findFirstByStatusOrderByIdAsc(true);
		Optional<TestQuestion> testQuestionCurrentOptional = testQuestionsRepository.findByIdAndTest(questionID, test);
		TestQuestion testQuestionCurrent  = testQuestionCurrentOptional.get();
		UserExamStatus examStatus = new UserExamStatus();
		String idealAnswer = testQuestionCurrent.getCorrectAnswer().trim();
		String actualAnswer = answer.trim();
		examStatus.setCorrectlyAnswered(idealAnswer.equals(actualAnswer));
		examStatus.setTestQuestion(testQuestionCurrent);
		examStatus.setUser(user);
		examStatus.setTest(test);
		examStatusRepository.save(examStatus);
		Optional<TestQuestion> testQuestionNext = testQuestionsRepository.findByIdAndTest(questionID+1, test);		
		if(testQuestionNext.isPresent() && testQuestionsRepository.countByTest(test)>=count) {
			List<TestAnswer> testAnswers  = testAnswersRepository.findAllByTestQuestion(testQuestionNext.get());	
			Collections.shuffle(testAnswers);
			model.addAttribute("count", count+1);
	    	model.addAttribute("test", test);
			model.addAttribute("testQuestion", testQuestionNext.get());
			model.addAttribute("testAnswers", testAnswers);
			model.addAttribute("timer", timer);
		}		
		else {
			
			model.addAttribute("showModal", true);
			model.addAttribute("userName", examUserDetails.getUsername());
			model.addAttribute("mark", getUserScore(user, test));
			return "user";
		}	    	
		model.addAttribute("userName", examUserDetails.getUsername());
		return "answerSheet";
	}
    
    //to init 1st question so A
    @PostMapping("/test")
    public String userTestPage(Model model, @AuthenticationPrincipal ExamUserDetails examUserDetails) {
		User user = userRepository.findByEmail(examUserDetails.getUsername());
    	Test test= testRepository.findFirstByStatusOrderByIdAsc(true);	
    	if(test != null) {
    		TestQuestion testQuestion = testQuestionsRepository.findTopByTestOrderByIdAsc(test);
    		List<TestAnswer> testAnswers = testAnswersRepository.findAllByTestQuestion(testQuestion);
    		if(examStatusRepository.countByUserAndTest(user, test)>0)
    			model.addAttribute("message", "Exam Already Taken, You May Not Take It Twice!!!"); 
    		else {
    			Collections.shuffle(testAnswers);
        		model.addAttribute("count", 1);
        		model.addAttribute("test", test);
        		model.addAttribute("testQuestion", testQuestion);
        		model.addAttribute("testAnswers", testAnswers);
        		model.addAttribute("timer", test.getDuration());
    		}
    	}else 
    		model.addAttribute("message", "No Exams Currently Active For You");    	
		model.addAttribute("userName", examUserDetails.getUsername());
    	return "answerSheet";
    }  
    
    //
    @GetMapping("/tests/{page}")
    public String uploadedTestsInit(Model model, @PathVariable("page") Optional<Integer> pageNo) {
    	int pageNumber = pageNo.isPresent() ? pageNo.get() : 0;
    	Pageable pageable = PageRequest.of(pageNumber, 15, Sort.by("createdOn").descending());
    	Slice<Test> testSlice = testRepository.findAll(pageable);
    	if(testSlice.hasContent()) {
    		model.addAttribute("tests", testSlice.getContent());
    		model.addAttribute("currentPage", testSlice.getNumber()+1);
    		model.addAttribute("hasNextPage", testSlice.hasNext());
    		model.addAttribute("hasNextPage", testSlice.hasPrevious());
    	}else
    		model.addAttribute("message", "No Tests, Upload Some To Edit Properties");
    	return "tests";
    }
    
    @PostMapping("/tests")
    public String activateTest(Model model, 
    		@RequestParam("status") boolean status,
    		@RequestParam("pageNo") int pageNo,
    		@RequestParam("id") long id) {
    	List<Test> testList =  new ArrayList<>();
    	Optional<Test> testCurrentlyActiveOptional = testRepository.findByStatus(true);
    	if(testCurrentlyActiveOptional.isPresent()) {
    		Test testCurrentlyActive = testCurrentlyActiveOptional.get();
        	testCurrentlyActive.setStatus(false);  
        	testList.add(testCurrentlyActive);  		
    	}
    	Test testNowActive = testRepository.findById(id).get();
    	testNowActive.setStatus(!status);
    	testList.add(testNowActive);
    	testRepository.saveAll(testList);
    	int pageNumber = pageNo-1;
    	Pageable pageable = PageRequest.of(pageNumber, 15, Sort.by("createdOn").descending());
    	Slice<Test> testSlice = testRepository.findAll(pageable);
    	if(testSlice.hasContent()) {
    		model.addAttribute("tests", testSlice.getContent());
    		model.addAttribute("currentPage", testSlice.getNumber()+1);
    		model.addAttribute("hasNextPage", testSlice.hasNext());
    		model.addAttribute("hasPreviousPage", testSlice.hasPrevious());
    	}else
    		model.addAttribute("message", "No Tests, Upload Some To Edit Properties");
    	return "tests";
    }
    
    @GetMapping("/participants/{pageNo}")
    public String participants(Model model, 
    		@PathVariable("pageNo") Optional<Integer> pageNo) {
    	int pageNumber = pageNo.isPresent()? pageNo.get() : 0 ;
    	Pageable pageable = PageRequest.of(pageNumber, 15);
    	Slice<UserExamStatus> examStatusSlice = examStatusRepository.findAll(pageable);
    	if(examStatusSlice.hasContent()) {
    		List<UserExamStatus> userExamStatuses = examStatusSlice.getContent();
    		//userExamStatuses.stream().forEach(System.out::println);
    		List<User> users = new ArrayList<>();
    		userExamStatuses.stream().forEach(x->{
    			User user = userRepository.findUserById(x.getUser().getId());
    			if(!users.contains(user))
    				users.add(user);
    			});
    		model.addAttribute("users", users);
    		model.addAttribute("currentPage", examStatusSlice.getNumber()+1);
    		model.addAttribute("hasNextPage", examStatusSlice.hasNext());
    		model.addAttribute("hasPreviousPage", examStatusSlice.hasPrevious());
    	}else
    		model.addAttribute("message", "If Users Write Tests, They Will Show Up Here");
    	return "participants";
    }
    
    @GetMapping("/participant/{id}")
    public String participation(@PathVariable("id") long id, Model model) {
    	User user = userRepository.findById(id).get();
    	List<Test> tests = new ArrayList<>();
    	List<Double> scores = new ArrayList<>();
    	List<UserExamStatus> userExamStatuses = examStatusRepository.findByUser(user);
    	userExamStatuses.forEach(s->{
    		Test test = testRepository.findById(s.getTest().getId()).get();
    		if(!tests.contains(test)) {
    			tests.add(test);
    			scores.add(getUserScore(user, test));
    		}
    	});    		    			
    	model.addAttribute("user", user);
    	model.addAttribute("tests", tests);
    	model.addAttribute("scores", scores);
    	return "participant";
    }
    
    @GetMapping(value = "/report/{id}", produces = org.springframework.http.MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getReport(@PathVariable("id") long id){
    	User user = userRepository.findUserById(id);
    	List<UserExamStatus> testsWritten = examStatusRepository.findByUser(user);
    	List<UserScore> userScores = new ArrayList<>();
    	Set<Test> tests = new HashSet<>();
    	testsWritten.forEach(t->tests.add(t.getTest()));
    	tests.forEach(t->userScores.add(new UserScore(t, getUserScore(user, t))));
    	HttpHeaders headers = new HttpHeaders();
    	headers.add("Content-Disposition", "inlinie;filename=report.pdf");
    	return ResponseEntity
    			.ok()
    			.headers(headers).
    			contentType(org.springframework.http.MediaType.APPLICATION_PDF).body(new InputStreamResource(PDFGeneratorService.generateCandidateReport(user, userScores)));
    }
    
    
	private Double getUserScore(User user, Test test) {
		int y = examStatusRepository.countByUserAndTest(user, test);
		int x = examStatusRepository.countByCorrectlyAnsweredAndUserAndTest(true, user, test);
		return ((double)x/y)*100.0;
	}
}
