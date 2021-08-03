package com.tapfuma.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tapfuma.entities.Test;
import com.tapfuma.entities.TestQuestion;

@Repository
public interface TestQuestionsRepository extends CrudRepository<TestQuestion, Long> {
	TestQuestion findTopByTestOrderByIdAsc(Test test);
	Optional<TestQuestion>  findByIdAndTest(Long id,Test test);
	int countByTest(Test test);
}
