package com.example.U5_W6_D5.repository;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.entity.Prenotazione;
import com.example.U5_W6_D5.entity.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {

    List<Prenotazione> findByDipendente(Dipendente dipendente);

    List<Prenotazione> findByViaggio(Viaggio viaggio);

    List<Prenotazione> findByDipendenteId(Long dipendenteId);

    List<Prenotazione> findByViaggioId(Long viaggioId);
}
