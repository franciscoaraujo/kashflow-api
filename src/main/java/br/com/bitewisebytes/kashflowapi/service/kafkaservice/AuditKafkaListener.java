package br.com.bitewisebytes.kashflowapi.service.kafkaservice;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.AuditTransaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class AuditKafkaListener {

    private static final Logger log = LoggerFactory.getLogger(AuditKafkaListener.class);

    private final AuditService auditService;

    public AuditKafkaListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @KafkaListener(
            topics = "wallet.audit.transaction",
            containerFactory = "auditKafkaListenerContainerFactory",
            groupId = "wallet-consumer-group"
    )
    public void listen(ConsumerRecord<String, TransactionWallet> record, Acknowledgment acknowledgment) {
        try {
            TransactionWallet transaction = record.value();
            log.info("🔍 Received audit event: {}", transaction);
            // Convert and save as needed
            acknowledgment.acknowledge();
        } catch (Exception ex) {
            log.error("❌ Error processing audit event, sending to DLT: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}