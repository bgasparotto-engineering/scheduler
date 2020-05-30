package com.bgasparotto.scheduler.messaging.producer;

import com.bgasparotto.scheduler.message.RunUpdate;
import com.bgasparotto.scheduler.messaging.KafkaTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RunUpdateProducerTest extends KafkaTest {

    @Autowired
    private RunUpdateProducer producer;

    @Value("${topics.output.run-hansard-update}")
    private String topic;

    private Consumer<String, RunUpdate> consumer;

    @BeforeEach
    public void setup() {
        consumer = createConsumer();
        consumer.subscribe(List.of(topic));
    }

    @Test
    public void shouldProduceUpdatedMessage() {
        RunUpdate testMessage = createTestMessage();
        producer.produce(testMessage);

        ConsumerRecord<String, RunUpdate> consumedRecord = consumeOne(consumer, topic);
        assertThat(consumedRecord.key()).isEqualTo(testMessage.getDescription());
        assertThat(consumedRecord.value()).isEqualTo(testMessage);
    }

    private RunUpdate createTestMessage() {
        return RunUpdate.newBuilder()
                .setDescription("Test message")
                .build();
    }
}
