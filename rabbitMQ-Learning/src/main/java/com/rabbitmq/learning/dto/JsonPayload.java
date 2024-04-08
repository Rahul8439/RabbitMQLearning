package com.rabbitmq.learning.dto;

import java.io.Serializable;

import lombok.Data;

@Data
public class JsonPayload implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
}
