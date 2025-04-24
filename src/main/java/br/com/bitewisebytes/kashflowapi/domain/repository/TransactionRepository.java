package br.com.bitewisebytes.kashflowapi.domain.repository;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionWallet, Long> {

    Optional<TransactionWallet> findByIdAndWalletId(Long id, Long walletId);

    Optional<TransactionWallet> findByIdAndWalletUserDocumentNumber(Long id, String documentNumber);

    @Query("SELECT t FROM TransactionWallet t JOIN t.wallet w WHERE w.user.documentNumber = :documentNumber AND t.dataTransaction = :dataTransaction")
    Optional<List<TransactionWallet>> findTransactionByDocumentoAndDataTransaction(String documentNumber, LocalDate dataTransaction, Pageable pageable);

}