package com.tapfuma.repositories;

import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.CrudRepository;

import com.tapfuma.entities.Test;
import com.tapfuma.entities.User;
import com.tapfuma.entities.UserExamStatus;

public interface TestRepository extends CrudRepository<Test, Long> {

	Test findFirstByStatusOrderByIdAsc(boolean status);
	Optional<Test> findByStatus(boolean status);
	Slice<Test> findAll(Pageable pageable);
	User findByUserExamStatus(UserExamStatus userExamStatus);
}
