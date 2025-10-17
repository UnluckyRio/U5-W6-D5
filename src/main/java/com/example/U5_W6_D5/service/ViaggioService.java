package com.example.U5_W6_D5.service;

import com.example.U5_W6_D5.entity.StatoViaggio;
import com.example.U5_W6_D5.entity.Viaggio;
import com.example.U5_W6_D5.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    // CREATE - Crea un nuovo viaggio
    public Viaggio createViaggio(Viaggio viaggio) {
        // Validazione base
        if (viaggio.getData() == null) {
            throw new IllegalArgumentException("La data del viaggio è obbligatoria");
        }
        if (viaggio.getDestinazione() == null || viaggio.getDestinazione().isEmpty()) {
            throw new IllegalArgumentException("La destinazione è obbligatoria");
        }
        if (viaggio.getStato() == null) {
            viaggio.setStato(StatoViaggio.IN_PROGRAMMA); // Stato predefinito
        }
        return viaggioRepository.save(viaggio);
    }

    // READ - Ottieni tutti i viaggi
    public List<Viaggio> getAllViaggi() {
        return viaggioRepository.findAll();
    }

    // READ - Ottieni un viaggio per ID
    public Optional<Viaggio> getViaggioById(Long id) {
        return viaggioRepository.findById(id);
    }

    // READ - Ottieni viaggi per stato
    public List<Viaggio> getViaggiByStato(StatoViaggio stato) {
        return viaggioRepository.findByStato(stato);
    }

    // READ - Ottieni viaggi per destinazione
    public List<Viaggio> getViaggiByDestinazione(String destinazione) {
        return viaggioRepository.findByDestinazione(destinazione);
    }

    // READ - Ottieni viaggi dopo una certa data
    public List<Viaggio> getViaggiAfterData(LocalDate data) {
        return viaggioRepository.findByDataAfter(data);
    }

    // READ - Ottieni viaggi in un intervallo di date
    public List<Viaggio> getViaggiBetweenDate(LocalDate dataInizio, LocalDate dataFine) {
        return viaggioRepository.findByDataBetween(dataInizio, dataFine);
    }

    // UPDATE - Aggiorna un viaggio esistente
    public Viaggio updateViaggio(Long id, Viaggio viaggioAggiornato) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    viaggio.setDestinazione(viaggioAggiornato.getDestinazione());
                    viaggio.setData(viaggioAggiornato.getData());
                    viaggio.setStato(viaggioAggiornato.getStato());

                    return viaggioRepository.save(viaggio);
                })
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + id));
    }

    // UPDATE - Aggiorna parzialmente un viaggio
    public Viaggio patchViaggio(Long id, Viaggio viaggioAggiornato) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    if (viaggioAggiornato.getDestinazione() != null) {
                        viaggio.setDestinazione(viaggioAggiornato.getDestinazione());
                    }
                    if (viaggioAggiornato.getData() != null) {
                        viaggio.setData(viaggioAggiornato.getData());
                    }
                    if (viaggioAggiornato.getStato() != null) {
                        viaggio.setStato(viaggioAggiornato.getStato());
                    }

                    return viaggioRepository.save(viaggio);
                })
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + id));
    }

    // UPDATE - Aggiorna solo lo stato del viaggio
    public Viaggio updateStatoViaggio(Long id, StatoViaggio nuovoStato) {
        return viaggioRepository.findById(id)
                .map(viaggio -> {
                    viaggio.setStato(nuovoStato);
                    return viaggioRepository.save(viaggio);
                })
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + id));
    }

    // DELETE - Elimina un viaggio per ID
    public void deleteViaggio(Long id) {
        if (!viaggioRepository.existsById(id)) {
            throw new RuntimeException("Viaggio non trovato con ID: " + id);
        }
        viaggioRepository.deleteById(id);
    }

    // Verifica se un viaggio esiste
    public boolean existsById(Long id) {
        return viaggioRepository.existsById(id);
    }
}

