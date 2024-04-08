package com.rabbitmq.learning.producer;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.learning.dto.JsonPayload;

@Service
public class RabbitMQProducer {
	
	/*
	 * @Value("${rabbitmq.topic-routing-key}") private String topicRoutingKey;
	 */
	/*
	 * @Autowired private FanoutExchange fanoutExchange;
	 * 
	 * @Autowired private TopicExchange topicExchange;
	 */
	@Autowired
	private DirectExchange directExchange;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	public String sendData(String msg, String routingKey) {
		JsonPayload payload=new JsonPayload();
		payload.setName(msg);
		
		//Direct Exchange
		rabbitTemplate.convertAndSend(directExchange.getName(),routingKey,payload);
		
		//Topic Exchange
		//rabbitTemplate.convertAndSend(topicExchange.getName(),routingKey, msg);
		
		//fanout Exchange
		//rabbitTemplate.convertAndSend(fanoutExchange.getName(),"", msg);
		return "Msg Sent Successfully..";
	}
}
