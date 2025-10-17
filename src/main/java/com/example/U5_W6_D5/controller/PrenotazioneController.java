package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.dto.PrenotazioneRequestDTO;
import com.example.U5_W6_D5.entity.Prenotazione;
import com.example.U5_W6_D5.exception.ResourceNotFoundException;
import com.example.U5_W6_D5.service.PrenotazioneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST per la gestione delle prenotazioni
 */
@RestController
@RequestMapping("/api/prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    /**
     * CREATE - Crea una nuova prenotazione
     */
    @PostMapping
    public ResponseEntity<Prenotazione> createPrenotazione(@Valid @RequestBody PrenotazioneRequestDTO requestDTO) {
        Prenotazione prenotazione = prenotazioneService.createPrenotazione(
                requestDTO.getDipendenteId(),
                requestDTO.getViaggioId(),
                requestDTO.getNotePreferenze()
        );
        return new ResponseEntity<>(prenotazione, HttpStatus.CREATED);
    }

    /**
     * READ - Ottieni tutte le prenotazioni
     */
    @GetMapping
    public ResponseEntity<List<Prenotazione>> getAllPrenotazioni() {
        List<Prenotazione> prenotazioni = prenotazioneService.getAllPrenotazioni();
        return ResponseEntity.ok(prenotazioni);
    }

    /**
     * READ - Ottieni una prenotazione per ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Prenotazione> getPrenotazioneById(@PathVariable Long id) {
        Prenotazione prenotazione = prenotazioneService.getPrenotazioneById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione", id));
        return ResponseEntity.ok(prenotazione);
    }

    /**
     * READ - Ottieni prenotazioni per dipendente
     */
    @GetMapping("/dipendente/{dipendenteId}")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniByDipendente(@PathVariable Long dipendenteId) {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniByDipendente(dipendenteId);
        return ResponseEntity.ok(prenotazioni);
    }

    /**
     * READ - Ottieni prenotazioni per viaggio
     */
    @GetMapping("/viaggio/{viaggioId}")
    public ResponseEntity<List<Prenotazione>> getPrenotazioniByViaggio(@PathVariable Long viaggioId) {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioniByViaggio(viaggioId);
        return ResponseEntity.ok(prenotazioni);
    }

    /**
     * DELETE - Elimina una prenotazione
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrenotazione(@PathVariable Long id) {
        prenotazioneService.deletePrenotazione(id);
        return ResponseEntity.noContent().build();
    }
}
