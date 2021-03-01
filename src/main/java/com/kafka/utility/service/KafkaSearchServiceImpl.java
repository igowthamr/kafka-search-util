package com.kafka.utility.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kafka.utility.model.Response;
import com.kafka.utility.model.SearchRequest;
import com.kafka.utility.repo.KafkaRepo;

@Component
public class KafkaSearchServiceImpl implements KafkaSearchService{

	@Autowired
	private KafkaRepo kafkaRepo;
	
	@Override
	public Response searchKafka(final SearchRequest request) {
		// TODO Auto-generated method stub
		return kafkaRepo.searchKafka(request);
	}

}
