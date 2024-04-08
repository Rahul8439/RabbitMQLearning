package com.rabbitmq.learning.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rabbitmq.learning.producer.RabbitMQProducer;

@RestController
@RequestMapping("/api/v1/rabbitmq")
public class TestRabbitMQController {

	@Autowired
	private RabbitMQProducer producer;

	@GetMapping("/send-msg")
	public ResponseEntity<String> sendMsg(@RequestParam String msg) {
		
		
		return new ResponseEntity<String>(producer.sendData(msg,"direct.routingKey"), HttpStatus.OK);
	}
}
