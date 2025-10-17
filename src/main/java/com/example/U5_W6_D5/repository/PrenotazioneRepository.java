package com.example.U5_W6_D5.repository;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.entity.Prenotazione;
import com.example.U5_W6_D5.entity.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByDipendente(Dipendente dipendente);

    List<Prenotazione> findByViaggio(Viaggio viaggio);

    List<Prenotazione> findByDipendenteId(Long dipendenteId);

    List<Prenotazione> findByViaggioId(Long viaggioId);

    // Metodo per verificare se un dipendente ha già prenotazioni per una specifica data
    @Query("SELECT p FROM Prenotazione p WHERE p.dipendente.id = :dipendenteId AND p.viaggio.data = :data")
    List<Prenotazione> findByDipendenteIdAndViaggioData(@Param("dipendenteId") Long dipendenteId, @Param("data") LocalDate data);

    // Metodo alternativo per verificare l'esistenza di prenotazioni
    boolean existsByDipendenteIdAndViaggioData(Long dipendenteId, LocalDate data);
}
