package com.tapfuma.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.tapfuma.dto.ExamUserDetails;
import com.tapfuma.entities.User;
import com.tapfuma.repositories.UserRepository;

@Service
public class ExamUserDetailsService implements UserDetailsService {

	@Autowired
    private UserRepository userRepository;
    
	@Override
	public ExamUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(username);
		if (user == null) {
            throw new UsernameNotFoundException("Could Not Find User: "+username);
        }
		ExamUserDetails examUserDetails = new ExamUserDetails(user);
        return examUserDetails;
	}
}
