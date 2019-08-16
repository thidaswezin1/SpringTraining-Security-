package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.demo.entity.User;
import com.example.demo.repository.EmployeeRepository;
@Service
@Transactional
public class UserService implements UserDetailsService {
	EmployeeRepository userRepo;
	PasswordEncoder passwordEncoder;
	
	public UserService(EmployeeRepository userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
		if(!userRepo.existsByEmail("thidaswezin@gmail.com")) {
			User user = new User();
			user.setName("ThidaSweZin");
			user.setEmail("thidaswezin@gmail.com");
			String encoded = passwordEncoder.encode("abcdef");
			System.out.println("Encoded Password : "+encoded);
			user.setPassword(encoded);
			user.setLevel("Manager");
			userRepo.save(user);
		}
		if(!userRepo.existsByEmail("thida@gmail.com")) {
			User user = new User();
			user.setName("Thida");
			user.setEmail("thida@gmail.com");
			String encoded = passwordEncoder.encode("abcdef");
			System.out.println("Encoded Password : "+encoded);
			user.setPassword(encoded);
			user.setLevel("Manager");
			userRepo.save(user);
		}
		if(!userRepo.existsByEmail("swezin@gmail.com")) {
			User user = new User();
			user.setName("SweZin");
			user.setEmail("swezin@gmail.com");
			String encoded = passwordEncoder.encode("abcdef");
			System.out.println("Encoded Password : "+encoded);
			user.setPassword(encoded);
			user.setLevel("Manager");
			userRepo.save(user);
		}
	}
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		System.out.println("Authenticate Email : "+email); 
		User user = userRepo.findByEmail(email).orElseThrow(()-> new BadCredentialsException("Email Don't exist"));
		System.out.println(passwordEncoder.matches("abcdef", user.getPassword()));
		List<GrantedAuthority> authorityList = new ArrayList<>();
		authorityList.add(new SimpleGrantedAuthority(user.getLevel()));
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorityList);	
	}
}
