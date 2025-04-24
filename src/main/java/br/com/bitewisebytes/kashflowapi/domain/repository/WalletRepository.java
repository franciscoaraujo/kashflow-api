package br.com.bitewisebytes.kashflowapi.domain.repository;



import br.com.bitewisebytes.kashflowapi.domain.model.entity.Wallet;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {

    Optional<Wallet> findByWalletNumber(String walletNumber);

    @Query("SELECT w FROM Wallet w join w.user u WHERE u.documentNumber = :documentNumber AND w.walletNumber = :walletNumber")
    Optional<Wallet> findByDocumentNumberAndWalletNumber(String documentNumber,String walletNumber);

    List<Wallet> findByUserDocumentNumber(String documentNumber, Pageable pageable);
}