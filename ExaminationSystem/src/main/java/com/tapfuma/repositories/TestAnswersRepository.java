package com.tapfuma.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tapfuma.entities.TestAnswer;
import com.tapfuma.entities.TestQuestion;

@Repository
public interface TestAnswersRepository extends CrudRepository<TestAnswer, Long> {
	List<TestAnswer> findAllByTestQuestion(TestQuestion tesQuestion);
}
