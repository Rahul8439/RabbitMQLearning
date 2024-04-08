package com.rabbitmq.learning.consumer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
public class RabbitMQConsumer {
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private DirectExchange directExchange;
	
	@Value("${rabbitmq.direct-routing-key}")
	private String directRoutingKey;
	
	@RabbitListener(queues = {"${rabbitmq.direct-queue}"})//,concurrency = "3"
	private void handleMessageDirectQueue(Message msg) {
		System.out.println("Thread Name::" + Thread.currentThread().getName());
		System.out.println(msg.getBody());
		String str = new String(msg.getBody(), StandardCharsets.UTF_8);
		if (str.equalsIgnoreCase("Hello World")) {
			System.out.println("Correct Msg");
		} else {
			throw new RuntimeException("Error");
		}

	}
	
	
	
	
	@RabbitListener(queues = {"${rabbitmq.direct-queue}"})
	private void Consumer2(Object msg) {
		System.out.println("Msg Recieved By Topic Queue: "+msg);
	}

/*	@RabbitListener(queues = {"${rabbitmq.fanout-queue}"})
	private void Consumer3(String msg) {
		System.out.println("Msg Recieved By Fanout Queue: "+msg);
	}*/
	
	@RabbitListener(queues = "${rabbitmq.dead-letter-queue}")
    public void handleMessage(Message message) {
		System.out.println("Dead queue executed");
        rabbitTemplate.convertAndSend(directExchange.getName(),directRoutingKey, "Hello World");
    }
}
