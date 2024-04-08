package com.rabbitmq.learning.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.ConditionalRejectingErrorHandler;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	@Value("${rabbitmq.direct-queue}")
	private String directQueue;

	@Value("${rabbitmq.direct-exchange}")
	private String directExchange;

	@Value("${rabbitmq.direct-routing-key}")
	private String directRoutingKey;
	
	@Value("${rabbitmq.dead-letter-queue}")
	private String deadLetterQueue;
	
	@Value("${rabbitmq.dead-letter-routing-key}")
	private String deadLetterRoutingKey;
	
	/*
	 * @Value("${rabbitmq.topic-queue}") private String topicQueue;
	 * 
	 * @Value("${rabbitmq.topic-exchange}") private String topicExchange;
	 * 
	 * @Value("${rabbitmq.topic-routing-key}") private String topicRoutingKey;
	 * 
	 * @Value("${rabbitmq.fanout-queue}") private String fanoutQueue;
	 * 
	 * @Value("${rabbitmq.fanout-exchange}") private String fanoutExchange;
	 */
	 
	
	/*
	 * @Value("${rabbitmq.json-queue-name}") private String jsonQueue;
	 */
	@Bean
	public Queue mainQueue() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-dead-letter-exchange", directExchange);
        args.put("x-dead-letter-routing-key", deadLetterRoutingKey);
        return new Queue(directQueue, true, false, false, args);
    }

	@Bean
	public Queue directQueue() {
		return QueueBuilder.durable(directQueue)
                .withArgument("x-dead-letter-exchange", directExchange)
                .withArgument("x-dead-letter-routing-key", deadLetterRoutingKey)
                .build();
	}
	
	@Bean
	public DirectExchange directExchange() {
		return new DirectExchange(directExchange);
	}
	
	@Bean
	public Binding directBinding() {
		return BindingBuilder.bind(mainQueue()).to(directExchange()).with(directRoutingKey);
	}
	
	@Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(deadLetterQueue).build();
    }
	
	
	@Bean
    public Binding deadLetterBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(directExchange())
                .with(deadLetterRoutingKey); // Routing key pattern to route all messages
    }
	
	
	/*
	 * @Bean public Queue topicQueue() { return new Queue(topicQueue); }
	 * 
	 * @Bean public Queue fanoutQueue() { return new Queue(fanoutQueue); }
	 */

	
	/*
	 * @Bean public TopicExchange topicExchange() { return new
	 * TopicExchange(topicExchange); }
	 * 
	 * @Bean public FanoutExchange fanoutExchange() { return new
	 * FanoutExchange(fanoutExchange); }
	 * 
	 * @Bean public Queue jsonQueue() { return new Queue(jsonQueue); }
	 */

	
	//Topic Exchange
	/*
	 * @Bean public Binding topicBinding1() { return
	 * BindingBuilder.bind(topicQueue()).to(topicExchange()).with("topic.#"); }
	 * 
	 * 
	 * @Bean public Binding topicBinding3() { return
	 * BindingBuilder.bind(directQueue()).to(topicExchange()).with("direct.*"); }
	 * 
	 * //Fanout Exchanges
	 * 
	 * @Bean public Binding fanoutBinding1() { return
	 * BindingBuilder.bind(fanoutQueue()).to(fanoutExchange()); }
	 * 
	 * //@Bean public Binding fanoutBinding2() { return
	 * BindingBuilder.bind(topicQueue()).to(fanoutExchange()); }
	 */
	@Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(converter()); // Use JSON message converter
        return rabbitTemplate;
    }
	
	@Bean
	public MessageConverter converter() {
		return new Jackson2JsonMessageConverter();
	}
	
	 
}
