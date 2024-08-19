package com.study.order.config;

import com.study.order.domain.event.DefaultAddressUpdateEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@EnableKafka
@Configuration
public class KafkaConfig {

	@Bean
	public KafkaTemplate<String, DefaultAddressUpdateEvent> kafkaTemplate(ProducerFactory<String, DefaultAddressUpdateEvent> producerFactory) {
		return new KafkaTemplate<>(producerFactory);
	}

}
