package com.example.demo.dto;

import lombok.Data;

@Data
public class KafkaMessageDTO {
    String key;
    String value;
}
