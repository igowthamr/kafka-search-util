package com.kafka.utility.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.kafka.utility.model.Message;
import com.kafka.utility.model.Response;
import com.kafka.utility.model.SearchRequest;
import com.kafka.utility.service.KafkaSearchService;

@RestController
public class KafkaMessageSearchController {

	@Autowired
	private KafkaSearchService kafkaService;
	
	@RequestMapping(value = "/search", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public Response search(@RequestBody final SearchRequest request){
		System.out.println("search");
		return kafkaService.searchKafka(request);
	}
	
	@RequestMapping(value = "/simple", method = RequestMethod.GET, produces = "application/json")
	public List<Message> simple(){
		System.out.println("search");
		Message m1 = new Message("",1234,12l,"");
		List<Message> list = new ArrayList<>();
		list.add(m1);
		m1.setMessage("Here");
		return list;
	}
}
