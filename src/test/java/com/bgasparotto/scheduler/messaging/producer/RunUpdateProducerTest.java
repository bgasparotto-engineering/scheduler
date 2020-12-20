package com.bgasparotto.scheduler.messaging.producer;

import com.bgasparotto.scheduler.message.RunUpdate;
import com.bgasparotto.spring.kafka.avro.test.EmbeddedKafkaAvro;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
@DirtiesContext
public class RunUpdateProducerTest {

    @Autowired
    private EmbeddedKafkaAvro embeddedKafkaAvro;

    @Autowired
    private RunUpdateProducer producer;

    @Value("${topics.output.run-hansard-update}")
    private String topic;

    @Test
    public void shouldProduceUpdatedMessage() {
        RunUpdate testMessage = buildTestMessage();
        producer.produce(testMessage);

        ConsumerRecord<String, RunUpdate> consumedRecord = embeddedKafkaAvro.consumeOne(topic);
        assertThat(consumedRecord.key()).isEqualTo(testMessage.getDescription());
        assertThat(consumedRecord.value()).isEqualTo(testMessage);
    }

    private RunUpdate buildTestMessage() {
        return RunUpdate.newBuilder()
                .setDescription("Test message")
                .build();
    }
}
