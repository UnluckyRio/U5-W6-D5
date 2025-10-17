package com.example.U5_W6_D5.repository;

import com.example.U5_W6_D5.entity.Dipendente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DipendenteRepository extends JpaRepository<Dipendente, Long> {

    Optional<Dipendente> findByUsername(String username);

    Optional<Dipendente> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
