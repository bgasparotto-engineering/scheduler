package com.bgasparotto.scheduler.messaging;

import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka
public class KafkaTest {
    private static final long DEFAULT_CONSUMER_TIMEOUT = Duration.of(5, ChronoUnit.SECONDS).toMillis();

    @Autowired
    private EmbeddedKafkaBroker embeddedKafkaBroker;

    @Value("${spring.kafka.consumer.key-deserializer}")
    private String keyDeserializerClass;

    @Value("${spring.kafka.consumer.value-deserializer}")
    private String valueDeserializerClass;

    @Value("${spring.kafka.properties.schema.registry.url}")
    private String schemaRegistryUrl;

    @Value("${spring.kafka.consumer.properties.specific.avro.reader}")
    private boolean specificAvroReader;

    protected Map<String, Object> consumerConfig() {
        var config = new HashMap<>(KafkaTestUtils.consumerProps("test-consumer", "false", embeddedKafkaBroker));

        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, keyDeserializerClass);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, valueDeserializerClass);
        config.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
        config.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, specificAvroReader);
        return config;
    }

    protected <K, V> Consumer<K, V> createConsumer() {
        return new DefaultKafkaConsumerFactory<K, V>(consumerConfig()).createConsumer();
    }

    protected <K, V> ConsumerRecord<K, V> consumeOne(Consumer<K, V> consumer, String topic) {
        return KafkaTestUtils.getSingleRecord(consumer, topic, DEFAULT_CONSUMER_TIMEOUT);
    }
}

