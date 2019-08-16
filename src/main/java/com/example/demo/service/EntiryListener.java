package com.example.demo.service;

import javax.persistence.PostPersist;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.example.demo.entity.User;

public class EntiryListener {
	@PrePersist
	@PreUpdate
	public void writePreLogs(User user) {
		//user.setName("Swe Zin");
		 
		System.out.println(String.format("The name of user pre save is %s",user.getName()));
		
	}
	
	@PostPersist
	@PostUpdate
	public void writePostLogs(User user) {
		System.out.println(String.format("The name of user post sav  is %s",user.getName()));
		//user.setName("Swe Zin");
		
	}
}