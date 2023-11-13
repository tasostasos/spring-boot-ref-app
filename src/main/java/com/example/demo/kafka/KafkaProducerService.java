package com.example.demo.kafka;

import com.example.demo.dto.KafkaMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

/**
 * Producer for kafka topic configured in application.yaml.
 *
 */
@Service
@Slf4j
public class KafkaProducerService {
    @Autowired
    private KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate;

    @Value("${app.kafka.producer.topic}")
    private String topic;

    public void produce(KafkaMessageDTO message) {
        log.info("message sent: {}", message);
        kafkaTemplate.send(topic, message);
    }
}
