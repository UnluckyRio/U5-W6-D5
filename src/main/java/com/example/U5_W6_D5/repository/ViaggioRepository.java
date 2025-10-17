package com.example.U5_W6_D5.repository;

import com.example.U5_W6_D5.entity.StatoViaggio;
import com.example.U5_W6_D5.entity.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {

    List<Viaggio> findByStato(StatoViaggio stato);

    List<Viaggio> findByDestinazione(String destinazione);

    List<Viaggio> findByDataAfter(LocalDate data);

    List<Viaggio> findByDataBetween(LocalDate dataInizio, LocalDate dataFine);
}

