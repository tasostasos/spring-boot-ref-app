package com.example.demo.kafka;

import com.example.demo.dto.KafkaMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class KafkaConsumerService {

    @KafkaListener(topics = {"#{'${app.kafka.consumer.topic}'.split(',')}"})
    public void consume(@Payload KafkaMessageDTO message) {
        log.info("Received Message in group foo: " + message);
    }
}
