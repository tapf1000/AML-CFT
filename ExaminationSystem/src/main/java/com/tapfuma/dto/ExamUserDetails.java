package com.tapfuma.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.tapfuma.entities.User;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ExamUserDetails implements UserDetails{

	User user;
	/**
	 * 
	 */
	
	private static final long serialVersionUID = 1L;

	public ExamUserDetails(User user) {
		super();
		this.user = user;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<>();
		if(!user.getEmail().equals("wbakasa@homelink.co.zw")) {
			SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
			authorities.add(authority);
		}
		else {
			SimpleGrantedAuthority authorityUser = new SimpleGrantedAuthority("ROLE_USER");
			SimpleGrantedAuthority authorityAdmin = new SimpleGrantedAuthority("ROLE_ADMIN");
			authorities.add(authorityUser);
			authorities.add(authorityAdmin);
		}			
		return authorities;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return user.getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return user.isAccountNonExpired();
	}

	@Override
	public boolean isAccountNonLocked() {
		return user.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return user.isCredentialNonExpired();
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return user.isEnabled();
	}

}
