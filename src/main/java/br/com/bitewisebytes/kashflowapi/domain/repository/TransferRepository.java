package br.com.bitewisebytes.kashflowapi.domain.repository;


import br.com.bitewisebytes.kashflowapi.domain.model.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {
}