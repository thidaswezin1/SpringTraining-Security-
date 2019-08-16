package com.example.demo.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.server.ResponseStatusException;

import com.example.demo.repository.EmployeeRepository;


@Aspect
@Component
public class ControllerAspect {
	@Autowired
	EmployeeRepository userRepo;
	
	@Before("target(com.example.demo.controller.MainController) && @annotation(org.springframework.web.bind.annotation.RequestMapping)"
			+ " && execution(* com.example.demo.controller.MainController.*(org.springframework.ui.Model,..))")
	public void beforeController(JoinPoint jp) {
		Model model = (Model)jp.getArgs()[0];
//		System.out.println("Before Process");
//		User user = userRepo.findById(1).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
		model.addAttribute("text", "Hello World");
	}
	
	@After("target(com.example.demo.controller.MainController) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public void afterController(JoinPoint jp) {
		System.out.println("After Process");
	}
	
	@Around("target(com.example.demo.controller.MainController) && @annotation(org.springframework.web.bind.annotation.RequestMapping)")
	public Object aroundController(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("Before around Process");
		Object result = pjp.proceed();
		System.out.println("After around Process");
		return result;
	}
	
	@Before("target(com.example.demo.controller.MainController) && @annotation(org.springframework.web.bind.annotation.GetMapping)"
			+ " && execution(* com.example.demo.controller.MainController.*(org.springframework.ui.Model,..))")
	public void beforeController1(JoinPoint jp) {
		//Model model = (Model)jp.getArgs()[0];
		System.out.println("Hello Thida Swe Zin");
//		User user = userRepo.findById(1).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
		//model.addAttribute("text", "Hello World");
	}
	
	@After("target(com.example.demo.controller.MainController) && @annotation(org.springframework.web.bind.annotation.GetMapping)")
	public void afterController1(JoinPoint jp) {
		System.out.println("Hello Thida");
	}
}
