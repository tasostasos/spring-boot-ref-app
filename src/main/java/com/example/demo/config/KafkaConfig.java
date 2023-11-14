package com.example.demo.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

/**
 * configuration class .that enables kafka.Any additional kafka configuration can be added here.
 *
 */
@Configuration
@EnableKafka
@Slf4j
public class KafkaConfig {

}
