package com.kafka.utility.repo;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.TopicDescription;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.OffsetAndTimestamp;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.TopicPartitionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import com.kafka.utility.model.Message;
import com.kafka.utility.model.Response;
import com.kafka.utility.model.SearchRequest;

@Component
public class KafkaRepoImpl implements KafkaRepo {

	@Autowired
	private ConsumerFactory<String, String> consumerFactory;

	@Autowired
	private AdminClient adminClient;

	@Override
	public Response searchKafka(final SearchRequest request) {
		List<Message> list = new ArrayList<>();
		Response response = new Response(list, null, null);
		Consumer<String, String> consClient = consumerFactory.createConsumer();
		Set<TopicPartition> set = consClient.assignment();
		Map<TopicPartition, Long> map=null;
		try {
			map = getTopicPartitionMap(request);
		} catch (InterruptedException | ExecutionException e) {
			response.setError("Error occurred while searching for message " + e.getMessage());
			response.setStatus("Failed");
			return response;
		}
		consClient.assign(map.keySet());
		Map<TopicPartition, OffsetAndTimestamp> offsetMap = consClient.offsetsForTimes(map);
		for (Entry<TopicPartition, OffsetAndTimestamp> entry : offsetMap.entrySet()) {
			consClient.seek(entry.getKey(), entry.getValue().offset());
			ConsumerRecords<String, String> records = consClient.poll(2000l);
			do {
				for (ConsumerRecord<String, String> record : records) {
					if (!isMatch(record.value(), request.getTxtToSearch()))
						continue;
					Message msg = new Message(record.topic(), record.partition(), record.offset(), record.value());
					list.add(msg);
				}
				records = consClient.poll(2000l);
			} while (!records.isEmpty());
		}
		if(!list.isEmpty()) {
			response.setStatus("Success");
			response.setMessages(list);
		}
		return response;
	}

	protected Map<TopicPartition, Long> getTopicPartitionMap(final SearchRequest request) throws InterruptedException, ExecutionException {
		List<String> topics = new ArrayList<>();
		topics.add(request.getTopicName());
		long start = request.getStart().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		//TODO:End not supported for now
		long end = request.getEnd().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
		Map<String, KafkaFuture<TopicDescription>> descTopics = adminClient.describeTopics(topics).values();
		Map<TopicPartition, Long> map = new HashMap<>();
		for (Entry<String, KafkaFuture<TopicDescription>> val : descTopics.entrySet()) {
			//System.out.println(val.getValue().get().partitions());
			List<TopicPartitionInfo> partitions = val.getValue().get().partitions();
			for (TopicPartitionInfo info : partitions) {
				map.put(new TopicPartition(request.getTopicName(), info.partition()), start);
			}
		}
		return map;
	}

	protected boolean isMatch(String value, String txtToSearch) {
		return value != null && value.contains(txtToSearch);
	}

}
