package com.tapfuma.repositories;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import com.tapfuma.entities.Test;
import com.tapfuma.entities.User;
import com.tapfuma.entities.UserExamStatus;

public interface UserExamStatusRepository extends CrudRepository<UserExamStatus, Long> {

	int countByCorrectlyAnsweredAndUserAndTest(boolean status,User user, Test test);
	int countByUserAndTest(User user, Test test);
	List<UserExamStatus> findByUser(User user);
	Slice<UserExamStatus> findAll(Pageable pageable);
	
}
