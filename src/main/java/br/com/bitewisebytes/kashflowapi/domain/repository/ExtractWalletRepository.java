package br.com.bitewisebytes.kashflowapi.domain.repository;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.TransactionWallet;
import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import br.com.bitewisebytes.kashflowapi.dto.ExtractWalletDto;
import br.com.bitewisebytes.kashflowapi.dto.ExtractWalletProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExtractWalletRepository extends JpaRepository<TransactionWallet, Long> {

    @Query("""
                SELECT t.id AS id,
                       t.createdAt AS createdAt,
                       t.dataTransaction AS dataTransaction,
                       t.description AS description,
                       t.type AS type,
                       t.amount AS amount,
                       tr.amount AS transferAmount,
                       w.walletNumber AS walletNumber,
                       rw.walletNumber AS receiverWalletNumber,
                       ru.name AS receiverUserName,
                       tr.createdAt AS transferCreatedAt
                FROM TransactionWallet t
                JOIN t.wallet w
                JOIN w.user u
                LEFT JOIN Transfer tr 
                    ON tr.senderTransactionWallet.id = t.id 
                    OR tr.receiverTransactionWallet.id = t.id
                LEFT JOIN Wallet rw ON rw.id = tr.receiverWallet.id
                LEFT JOIN User ru ON ru.id = rw.user.id
                WHERE w.walletNumber = :walletNumber
                  AND (
                       t.type in('DEPOSIT', 'WITHDRAW','TRANSFER_IN', 'TRANSFER_OUT')
                       OR tr IS NOT NULL
                  )
                ORDER BY t.createdAt DESC
            """)
    Optional<List<ExtractWalletProjection>> findExtractByWalletNumber(@Param("walletNumber") String walletNumber, Pageable pageable);

    @Query("""
    SELECT t.id AS id,
           t.createdAt AS createdAt,
           t.dataTransaction AS dataTransaction,
           t.description AS description,
           t.type AS type,
           t.amount AS amount,
           tr.amount AS transferAmount,
           w.walletNumber AS walletNumber,
           rw.walletNumber AS receiverWalletNumber,
           ru.name AS receiverUserName,
           tr.createdAt AS transferCreatedAt
    FROM TransactionWallet t
    JOIN t.wallet w
    JOIN w.user u
    LEFT JOIN Transfer tr
        ON tr.senderTransactionWallet.id = t.id
        OR tr.receiverTransactionWallet.id = t.id
    LEFT JOIN Wallet rw ON rw.id = tr.receiverTransactionWallet.wallet.id
    LEFT JOIN User ru ON ru.id = rw.user.id
    WHERE w.walletNumber = :walletNumber
      AND t.dataTransaction BETWEEN :initialDate AND :finalDate
      AND (
           t.type = 'DEPOSIT'
           OR tr IS NOT NULL
      )
    ORDER BY t.createdAt DESC
""")
    Optional<List<ExtractWalletProjection>> findExtractByWalletNumberAndaDate(
            @Param("walletNumber") String walletNumber,
            @Param("initialDate") LocalDate initialDate,
            @Param("finalDate") LocalDate finalDate,
            Pageable pageable
    );
}

