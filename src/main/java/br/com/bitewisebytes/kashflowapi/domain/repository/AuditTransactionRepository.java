package br.com.bitewisebytes.kashflowapi.domain.repository;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.AuditTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditTransactionRepository  extends JpaRepository<AuditTransaction, Long> {
}
