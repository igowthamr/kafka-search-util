package com.kafka.utility.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Response {

	List<Message> messages;
	String status;
	String error;
	
	
}
