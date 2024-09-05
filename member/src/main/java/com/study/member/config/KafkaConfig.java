package com.study.member.config;

import com.study.member.domain.event.AddressEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, AddressEvent> kafkaListenerContainerFactory(
			ConsumerFactory<String, AddressEvent> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, AddressEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();

		factory.setConsumerFactory(consumerFactory);
		return factory;
	}
}
