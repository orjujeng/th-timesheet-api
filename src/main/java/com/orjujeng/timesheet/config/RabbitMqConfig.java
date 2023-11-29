package com.orjujeng.timesheet.config;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
//	@Autowired
//	RabbitTemplate rabbitTemplate;
	@Bean
	public MessageConverter messageConverter(){
		return new Jackson2JsonMessageConverter();
	}
	
//	@PostConstruct
//	public void initRabbitTemplate() {
//		rabbitTemplate.setConfirmCallback(new ConfirmCallback() {
//			
//			@Override
//			public void confirm(CorrelationData correlationData, boolean ack, String cause) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//		
//		rabbitTemplate.setReturnCallback(new ReturnCallback() {
//			
//			@Override
//			public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
//	}
}
