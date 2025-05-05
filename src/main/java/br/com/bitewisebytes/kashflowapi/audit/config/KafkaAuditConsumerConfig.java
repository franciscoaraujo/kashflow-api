package br.com.bitewisebytes.kashflowapi.audit.config;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.util.backoff.FixedBackOff;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaAuditConsumerConfig {


    private static final String BOOTSTRAP_SERVERS = "wallet_kafka:9092"; // or "wallet_kafka:9092" in Docker
    //private static final String BOOTSTRAP_SERVERS = "localhost:9093"; //
    private static final String GROUP_ID = "wallet-consumer-group";

    @Bean
    public ConsumerFactory<String, TransactionWallet> auditConsumerFactory() {
        JsonDeserializer<TransactionWallet> deserializer = new JsonDeserializer<>(TransactionWallet.class);
        deserializer.addTrustedPackages("*");
        deserializer.setUseTypeMapperForKey(false);

        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
    }

    @Bean
    public ProducerFactory<String, TransactionWallet> auditProducerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, TransactionWallet> auditKafkaTemplate() {
        return new KafkaTemplate<>(auditProducerFactory());
    }

    @Bean(name = "auditKafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, TransactionWallet> auditKafkaListenerContainerFactory(
            KafkaTemplate<String, TransactionWallet> auditKafkaTemplate) {

        ConcurrentKafkaListenerContainerFactory<String, TransactionWallet> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(auditConsumerFactory());
        factory.getContainerProperties().setAckMode(
                org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL
        );

        DeadLetterPublishingRecoverer recoverer = new DeadLetterPublishingRecoverer(
                auditKafkaTemplate,
                (record, ex) -> new TopicPartition(record.topic() + ".DLT", record.partition())
        );

        factory.setCommonErrorHandler(new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3)));

        return factory;
    }
}