package br.com.bitewisebytes.kashflowapi.service.kafkaservice;


import br.com.bitewisebytes.kashflowapi.domain.model.entity.AuditTransaction;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionStatus;
import br.com.bitewisebytes.kashflowapi.domain.model.enums.TransactionType;
import br.com.bitewisebytes.kashflowapi.domain.repository.AuditTransactionRepository;
import br.com.bitewisebytes.kashflowapi.domain.repository.TransactionRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuditService {

    private static final Logger log = LoggerFactory.getLogger(AuditService.class);

    private final AuditTransactionRepository auditRepository;
    private final KafkaTemplate<String, TransactionWallet> kafkaTemplateWallet;

    public AuditService(AuditTransactionRepository auditRepository, KafkaTemplate<String, TransactionWallet> kafkaTemplateWallet) {
        this.auditRepository = auditRepository;
        this.kafkaTemplateWallet = kafkaTemplateWallet;
    }

    @Retry(
            name = "kafkaProducerRetry",
            fallbackMethod = "sendAuditFallback")
    @CircuitBreaker(
            name = "kafkaCircuitBreaker",
            fallbackMethod = "sendAuditFallback")
    public void logAudit(
        Wallet wallet,
        TransactionType type,
        BigDecimal amount,
        String description
    ) {
        TransactionWallet autidWalletTransaction = new TransactionWallet();
        autidWalletTransaction.setWallet(wallet);
        autidWalletTransaction.setType(type);
        autidWalletTransaction.setAmount(amount);
        autidWalletTransaction.setDescription(description);
        autidWalletTransaction.setDataTransaction(LocalDate.now());

        log.info("[AUDIT] Enviando evento para Kafka: {}", autidWalletTransaction);


        kafkaTemplateWallet.send("wallet.audit.transaction", "wallet.audit.transaction", autidWalletTransaction);
    }

    public void sendAuditFallback(
            Wallet wallet,
            TransactionType type,
            BigDecimal amount,
            String description,
            Throwable ex
    ) {
        log.error("[FALLBACK] Kafka indisponível. Salvando auditoria no banco de dados local.", ex);
        AuditTransaction audit = new AuditTransaction();
        audit.setTransactionId(UUID.randomUUID().toString());

        auditRepository.save(audit);
    }

    public void save(AuditTransaction transaction) {
        auditRepository.save(transaction);
    }
}
