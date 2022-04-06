package org.kafkainaction.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class HelloWorldProducer {

	private static final String TOPIC_NAME = "kinaction_helloworld";

	public static void main(String[] args) {

		Properties kaProperties = new Properties(); // <1>
		kaProperties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093,localhost:9094"); // <2>

		kaProperties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer"); // <3>
		kaProperties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
				"org.apache.kafka.common.serialization.StringSerializer");

		try (Producer<String, String> producer = new KafkaProducer<>(kaProperties)) { // <4>

			ProducerRecord<String, String> producerRecord = new ProducerRecord<>(TOPIC_NAME, null,
					"hello world again!"); // <5>

			producer.send(producerRecord); // <6>
			producer.close(); // <7>
		}
	}
}
