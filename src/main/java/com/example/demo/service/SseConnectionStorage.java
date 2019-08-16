package com.example.demo.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class SseConnectionStorage {
	public static List<SseEmitter> liveEmitters = new ArrayList<>();
	
	public static void addEmitter(SseEmitter emitter) {
		liveEmitters.add(emitter);
	}
	public static void removeEmitter(SseEmitter emitter) {
		liveEmitters.remove(emitter);
	}
	public static List<SseEmitter> getEmitters(){
		return liveEmitters;
	}
}
