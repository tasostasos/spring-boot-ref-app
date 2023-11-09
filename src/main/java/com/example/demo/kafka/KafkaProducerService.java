package com.example.demo.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.kafka.producer.topic}")
    private String topic;

    public void produce(String message) {
        log.info("message sent: {}", message);
        kafkaTemplate.send(topic, message);
    }
}
