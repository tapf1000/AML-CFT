package com.tapfuma.dto;

import org.springframework.stereotype.Service;

import com.tapfuma.entities.Test;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserScore {
	Test test;
	Double score;
}
