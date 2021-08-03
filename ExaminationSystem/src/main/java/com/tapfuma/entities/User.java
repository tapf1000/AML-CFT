package com.tapfuma.entities;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	Long id;
	String name;
	String surname;
	String email;
	String password;
	LocalDate createdOn;
	boolean accountNonExpired;
	boolean enabled;
	boolean credentialNonExpired;
	boolean accountNonLocked;
	@OneToMany(mappedBy = "user" ,cascade = CascadeType.ALL)
	Set<UserExamStatus> UserExamStatuses = new HashSet<>();
	
	@PrePersist
	void createdOn(){
		createdOn = LocalDate.now();
	}
}
