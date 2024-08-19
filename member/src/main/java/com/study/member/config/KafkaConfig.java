package com.study.member.config;

import com.study.member.domain.event.DefaultAddressUpdateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, DefaultAddressUpdateEvent> kafkaListenerContainerFactory(
			ConsumerFactory<String, DefaultAddressUpdateEvent> consumerFactory) {

		ConcurrentKafkaListenerContainerFactory<String, DefaultAddressUpdateEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
		
		factory.setConsumerFactory(consumerFactory);
		return factory;
	}
}
