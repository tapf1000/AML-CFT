package com.tapfuma.repositories;

import org.springframework.data.repository.CrudRepository;

import com.tapfuma.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByEmail(String username);
	User findUserById(long id);
}
