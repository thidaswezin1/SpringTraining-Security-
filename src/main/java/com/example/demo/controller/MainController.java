package com.example.demo.controller;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter.SseEventBuilder;
import com.example.demo.entity.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.service.SseConnectionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class MainController {
	private ObjectMapper mapper1;

	@Autowired
	ObjectMapper anotherMapper;
	
	@Autowired
	ObjectMapper mapper;

	@Autowired
	EmployeeRepository userRepo;

	Logger logger = LoggerFactory.getLogger(MainController.class);

	public MainController(ObjectMapper mapper) {
		this.mapper1 = mapper;
	}

	/*
	 * @GetMapping("/") public String index(Model model,@RequestParam
	 * Optional<Integer> id) {
	 * 
	 * if(mapper != null) {
	 * logger.info("Dependency succefully injected from constructor"); }
	 * if(anotherMapper != null) {
	 * logger.info("Dependency succefully injected from annotation"); }
	 * if(mapper.equals(anotherMapper)) { logger.info("Injection are same"); }
	 * 
	 * User user = new User(); if(id.isPresent()) { user =
	 * userRepo.findById(id.get()).orElse(new User()); } model.addAttribute("user",
	 * user); return "index"; }
	 */

	@PostMapping("/save")
	public String doSomething(User user) {
		userRepo.save(user);
		return "redirect:/";
	}

	@RequestMapping(value = "/aspect")
	public String aspect(Model model) {
		return "aspect";
	}

	@RequestMapping(value = "/another")
	public String aspect() {
		return "aspect";
	}

	@GetMapping(value = "/aspectTest")
	public String aspectTest(Model model) {
		return "aspect";
	}

	@GetMapping("/login")
	public String login(Model model) {
		User user = new User();
		model.addAttribute("user", user);
		return "login";
	}

	@GetMapping("/")
	public String saveStudent(Model model, Authentication auth, @RequestParam Optional<Integer> id) {
		User user = new User();
		if (id.isPresent()) {
			user = userRepo.findById(id.get()).orElse(new User());
			id.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		}
		if (auth.isAuthenticated()) {
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			model.addAttribute("loggedInUser", userDetail);
			SseConnectionStorage.getEmitters().forEach(emitter->{
				//User loggedInUser = userRepo.findByEmail(userDetail.getUsername()).orElse(new User());
				try {
					//String userString = mapper.writeValueAsString(loggedInUser);
					String userString = mapper.writeValueAsString(userDetail);
					SseEventBuilder builder = SseEmitter.event().id("userLogin").data(userString, MediaType.APPLICATION_JSON)
							.comment("Welcome Thida ").name("User Login");
					emitter.send(builder);
				}
				catch(IOException e) {
					e.printStackTrace();
				}
			});
		}

		model.addAttribute("user", user);
		
		
		return "index";

	}
	
	@GetMapping("/oauth")
	public String oauth(Authentication auth, Model model) {
		if(auth instanceof OAuth2AuthenticationToken) {
			OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) auth;
			OAuth2User oauthUser = token.getPrincipal();
			for(Entry <String,Object> entry : oauthUser.getAttributes().entrySet()) {
				System.out.println(entry.getKey()+" : "+entry.getValue().toString());
			}
		}
		model.addAttribute("user",new User());
		return "index";
	}
	
	@GetMapping("/sse")
	public SseEmitter sseEvents() {
		SseEmitter emitter = new SseEmitter();
		emitter.onCompletion(()->{SseConnectionStorage.removeEmitter(emitter);});
		emitter.onTimeout(()->{SseConnectionStorage.removeEmitter(emitter);});
		SseConnectionStorage.addEmitter(emitter);
		return emitter;
	}

}
