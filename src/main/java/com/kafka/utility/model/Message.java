package com.kafka.utility.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class Message {

	String topicName;
	int partition;
	long offset;
	String message;
	
	
}
