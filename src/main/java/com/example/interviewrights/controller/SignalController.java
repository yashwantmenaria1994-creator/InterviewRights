package com.example.interviewrights.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class SignalController {

	@MessageMapping("/offer/{room}")
	@SendTo("/topic/offer")
	public String offer(String msg) {
		return msg;
	}

	@MessageMapping("/answer/{room}")
	@SendTo("/topic/answer")
	public String answer(String msg) {
		return msg;
	}

	@MessageMapping("/ice/{room}")
	@SendTo("/topic/ice")
	public String ice(String msg) {
		return msg;
	}

}