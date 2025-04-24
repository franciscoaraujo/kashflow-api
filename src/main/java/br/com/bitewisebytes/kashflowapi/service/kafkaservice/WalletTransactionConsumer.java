package br.com.bitewisebytes.kashflowapi.service.kafkaservice;


import br.com.bitewisebytes.kashflowapi.domain.model.entity.AuditTransaction;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import br.com.bitewisebytes.kashflowapi.dto.WalletTransactionEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class WalletTransactionConsumer {

    private static final Logger log = LoggerFactory.getLogger(WalletTransactionConsumer.class);

    private final ObjectMapper objectMapper;
    private final TransactionRepository auditRepository;

    public WalletTransactionConsumer(ObjectMapper objectMapper, TransactionRepository auditRepository) {
        this.objectMapper = objectMapper;
        this.auditRepository = auditRepository;
    }

    @KafkaListener(topics = "wallet.audit.transaction", groupId = "wallet-consumer-group")
    public void consumeTransaction(ConsumerRecord<String, String> record, Acknowledgment ack) {
        try {
            String message = record.value();
            TransactionWallet event = objectMapper.readValue(message, TransactionWallet.class);
            TransactionWallet autidWalletTransaction = new TransactionWallet();
            autidWalletTransaction.setWallet(event.getWallet());
            autidWalletTransaction.setType(event.getType());
            autidWalletTransaction.setAmount(event.getAmount());
            autidWalletTransaction.setDescription(event.getDescription());
            autidWalletTransaction.setDataTransaction(LocalDate.now());

            log.info("✅ Evento salvo na auditoria: " + autidWalletTransaction.getId());
            ack.acknowledge();
        } catch (Exception e) {
            log.error("❌ Erro ao processar e salvar evento Kafka: " + e.getMessage());

        }
    }
}
