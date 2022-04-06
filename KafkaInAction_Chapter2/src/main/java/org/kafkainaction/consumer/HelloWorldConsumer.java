package org.kafkainaction.consumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloWorldConsumer {

	private static final Class<StringDeserializer> STRING_DESERALIZER = org.apache.kafka.common.serialization.StringDeserializer.class;

	private static final String CONSUMER_GROUP_ID = "kinaction_helloconsumer";

	static final Logger log = LoggerFactory.getLogger(HelloWorldConsumer.class.getName());

	private volatile boolean keepConsuming = true;

	public static void main(String[] args) {
		Properties kaProperties = new Properties(); // <1>
		kaProperties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094");
		kaProperties.put(ConsumerConfig.GROUP_ID_CONFIG, CONSUMER_GROUP_ID);
		kaProperties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
		kaProperties.put(ConsumerConfig.AUTO_COMMIT_INTERVAL_MS_CONFIG, "1000");
		kaProperties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, STRING_DESERALIZER);
		kaProperties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, STRING_DESERALIZER);

		HelloWorldConsumer helloWorldConsumer = new HelloWorldConsumer();
		helloWorldConsumer.consume(kaProperties);
		Runtime.getRuntime().addShutdownHook(new Thread(helloWorldConsumer::shutdown));
	}

	private void consume(Properties kaProperties) {
		try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(kaProperties)) {
			consumer.subscribe(List.of("kinaction_helloworld")); // <2>

			while (keepConsuming) {
				ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(250)); // <3>
				for (ConsumerRecord<String, String> record : records) { // <4>
					log.info("kinaction_info offset = {}, kinaction_value = {}", record.offset(), record.value());
				}
			}
		}
	}

	private void shutdown() {
		keepConsuming = false;
	}
}
