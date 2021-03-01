package com.kafka.utility.repo;

import com.kafka.utility.model.Response;
import com.kafka.utility.model.SearchRequest;

public interface KafkaRepo {
	
	public Response searchKafka(final SearchRequest request);

}
