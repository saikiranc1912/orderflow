package com.orderflow.inventory_service.config;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.util.backoff.FixedBackOff;

@Configuration
public class KafkaConsumerConfig {

    private static final Logger logger =
            LoggerFactory.getLogger(KafkaConsumerConfig.class);

    @Bean
    public DefaultErrorHandler kafkaErrorHandler(
            KafkaTemplate<String, Object> kafkaTemplate) {

        DeadLetterPublishingRecoverer recoverer =
                new DeadLetterPublishingRecoverer(
                        kafkaTemplate,
                        (record, exception) -> {

                            logger.error(
                                    "Publishing failed Kafka message to DLT. Topic: {}, Partition: {}, Offset: {}",
                                    record.topic(),
                                    record.partition(),
                                    record.offset(),
                                    exception
                            );

                            return new org.apache.kafka.common.TopicPartition(
                                    record.topic() + ".DLT",
                                    record.partition()
                            );
                        }
                );

        FixedBackOff fixedBackOff =
                new FixedBackOff(1000L, 2L);

        return new DefaultErrorHandler(
                recoverer,
                fixedBackOff
        );
    }
}