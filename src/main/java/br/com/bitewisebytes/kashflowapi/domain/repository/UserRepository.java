package br.com.bitewisebytes.kashflowapi.domain.repository;

import br.com.bitewisebytes.kashflowapi.domain.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByDocumentNumber(String documentNumber);

    Optional<User> findByEmailAndDocumentNumber(String email, String documentNumber);

    @Query("SELECT u FROM User u WHERE u.email = ?1")
    Optional<User>findByEmail(String email);
}
