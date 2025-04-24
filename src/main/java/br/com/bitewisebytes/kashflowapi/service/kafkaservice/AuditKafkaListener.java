package br.com.bitewisebytes.kashflowapi.service.kafkaservice;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.AuditTransaction;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
            topics = "wallet.audit",
            containerFactory = "auditKafkaListenerContainerFactory",
            groupId = "wallet-consumer-group"
    )
    public void listen(ConsumerRecord<String, AuditTransaction> record, Acknowledgment acknowledgment) {
        try {
            AuditTransaction transaction = record.value();
            log.info("🔍 Recebido evento de auditoria: {}", transaction);
            auditService.save(transaction);
            acknowledgment.acknowledge();

        } catch (Exception ex) {
            log.error("❌ Erro ao processar evento de auditoria, enviando para DLT: {}", ex.getMessage(), ex);
            throw ex;
        }
    }
}
