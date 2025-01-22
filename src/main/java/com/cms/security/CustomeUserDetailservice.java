package com.cms.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.cms.exceptions.UsernameNotFoundException;
import com.cms.repository.UserRepository;

import jakarta.websocket.server.ServerEndpoint;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CustomeUserDetailservice implements UserDetailsService {

	private UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return userRepo.findByUsername(username).map(CustomeUserDetails::new

		).orElseThrow(() -> new UsernameNotFoundException("user not found"));
	}
}
