package com.example.demo.kafka;

import com.example.demo.dto.KafkaMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class KafkaProducerServiceTest {

    @InjectMocks
    KafkaProducerService kafkaProducerService;

    @Mock
    KafkaTemplate<String, KafkaMessageDTO> kafkaTemplate;


    @Value("${app.kafka.producer.topic}")
    private String topic;

    @Test
    void produce() {
        KafkaMessageDTO dto = new KafkaMessageDTO();
        dto.setValue("value");
        dto.setKey("key");
        kafkaProducerService.produce(dto);
        verify(kafkaTemplate).send(topic, dto);
    }
}