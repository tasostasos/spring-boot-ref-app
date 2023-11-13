package com.example.demo.kafka;

import com.example.demo.dto.KafkaMessageDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class KafkaConsumerServiceTest {

    @InjectMocks
    private KafkaConsumerService kafkaConsumerService;

    @Test
    public void testConsume() {
        KafkaMessageDTO message = new KafkaMessageDTO();
        message.setKey("key");
        message.setValue("value");
        kafkaConsumerService.consume(message);
    }

}