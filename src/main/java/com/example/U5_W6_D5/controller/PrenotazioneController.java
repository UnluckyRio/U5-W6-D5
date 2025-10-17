package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.dto.PrenotazioneRequestDTO;
import com.example.U5_W6_D5.entity.Prenotazione;
import com.example.U5_W6_D5.service.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione delle prenotazioni
 */
@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    /**
     * CREATE - Crea una nuova prenotazione assegnando un dipendente a un viaggio
     * POST /api/prenotazioni
     *
     * @param requestDTO DTO contenente l'ID del dipendente, l'ID del viaggio e le note
     * @return La prenotazione creata
     */
    @PostMapping
    public ResponseEntity<?> createPrenotazione(@Valid @RequestBody PrenotazioneRequestDTO requestDTO) {
        try {
            Prenotazione prenotazione = prenotazioneService.createPrenotazione(
                    requestDTO.getDipendenteId(),
                    requestDTO.getViaggioId(),
                    requestDTO.getNotePreferenze()
            );
            return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        } catch (IllegalStateException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * READ - Ottieni tutte le prenotazioni
     * GET /api/prenotazioni
     *
     * @return Lista di tutte le prenotazioni
     */
    @GetMapping
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneService.getAllPrenotazioni();
        return new ResponseEntity<>(prenotazioni, HttpStatus.OK);
    }

    /**
     * READ - Ottieni una prenotazione per ID
     * GET /api/prenotazioni/{id}
     *
     * @param id ID della prenotazione
     * @return La prenotazione trovata
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getPrenotazioneById(@PathVariable Long id) {
        var prenotazioneOpt = prenotazioneService.getPrenotazioneById(id);
        if (prenotazioneOpt.isPresent()) {
            return new ResponseEntity<>(prenotazioneOpt.get(), HttpStatus.OK);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Prenotazione non trovata con ID: " + id);
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    }
}
