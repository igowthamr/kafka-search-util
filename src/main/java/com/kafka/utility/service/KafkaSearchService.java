package com.kafka.utility.service;

import com.kafka.utility.model.Response;
import com.kafka.utility.model.SearchRequest;

public interface KafkaSearchService {
	
	public Response searchKafka(final SearchRequest request);

}
