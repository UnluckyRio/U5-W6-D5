package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.dto.StatoViaggioUpdateDTO;
import com.example.U5_W6_D5.entity.StatoViaggio;
import com.example.U5_W6_D5.entity.Viaggio;
import com.example.U5_W6_D5.service.ViaggioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione dei viaggi
 */
@RestController
@RequestMapping("/api/viaggi")
public class ViaggioController {

    @Autowired
    private ViaggioService viaggioService;

    /**
     * Crea un nuovo viaggio
     *
     * @param viaggio Viaggio da creare
     * @return Il viaggio creato
     */
    @PostMapping
    public ResponseEntity<?> createViaggio(@Valid @RequestBody Viaggio viaggio) {
        try {
            Viaggio created = viaggioService.createViaggio(viaggio);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Ottiene tutti i viaggi
     *
     * @return Lista di tutti i viaggi
     */
    @GetMapping
    public ResponseEntity<List<Viaggio>> getAllViaggi() {
        return ResponseEntity.ok(viaggioService.getAllViaggi());
    }

    /**
     * Ottiene un viaggio per ID
     *
     * @param id ID del viaggio
     * @return Il viaggio trovato
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getViaggioById(@PathVariable Long id) {
        return viaggioService.getViaggioById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));
    }

    /**
     * Ottiene viaggi per stato
     *
     * @param stato Stato del viaggio (IN_PROGRAMMA o COMPLETATO)
     * @return Lista dei viaggi con lo stato specificato
     */
    @GetMapping("/stato/{stato}")
    public ResponseEntity<List<Viaggio>> getViaggiByStato(@PathVariable StatoViaggio stato) {
        return ResponseEntity.ok(viaggioService.getViaggiByStato(stato));
    }

    /**
     * Ottiene viaggi per destinazione
     *
     * @param destinazione Destinazione del viaggio
     * @return Lista dei viaggi con la destinazione specificata
     */
    @GetMapping("/destinazione/{destinazione}")
    public ResponseEntity<List<Viaggio>> getViaggiByDestinazione(@PathVariable String destinazione) {
        return ResponseEntity.ok(viaggioService.getViaggiByDestinazione(destinazione));
    }

    /**
     * Aggiorna completamente un viaggio
     *
     * @param id      ID del viaggio
     * @param viaggio Dati aggiornati del viaggio
     * @return Il viaggio aggiornato
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateViaggio(@PathVariable Long id, @RequestBody Viaggio viaggio) {
        try {
            Viaggio updated = viaggioService.updateViaggio(id, viaggio);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Aggiorna lo stato di un viaggio
     * Permette di cambiare lo stato da 'IN_PROGRAMMA' a 'COMPLETATO' e viceversa
     *
     * @param id       ID del viaggio
     * @param statoDTO DTO contenente il nuovo stato
     * @return Il viaggio con lo stato aggiornato
     */
    @PatchMapping("/{id}/stato")
    public ResponseEntity<?> updateStatoViaggio(
            @PathVariable Long id,
            @Valid @RequestBody StatoViaggioUpdateDTO statoDTO) {
        try {
            Viaggio viaggio = viaggioService.updateStatoViaggio(id, statoDTO.getStato());
            return ResponseEntity.ok(viaggio);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * Elimina un viaggio
     *
     * @param id ID del viaggio da eliminare
     * @return Messaggio di conferma
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteViaggio(@PathVariable Long id) {
        try {
            viaggioService.deleteViaggio(id);
            return ResponseEntity.ok(Map.of("message", "Viaggio eliminato con successo"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
