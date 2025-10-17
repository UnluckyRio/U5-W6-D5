package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.dto.StatoViaggioUpdateDTO;
import com.example.U5_W6_D5.entity.StatoViaggio;
import com.example.U5_W6_D5.entity.Viaggio;
import com.example.U5_W6_D5.exception.ResourceNotFoundException;
import com.example.U5_W6_D5.service.ViaggioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
     */
    @PostMapping
    public ResponseEntity<Viaggio> createViaggio(@Valid @RequestBody Viaggio viaggio) {
        Viaggio created = viaggioService.createViaggio(viaggio);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * Ottiene tutti i viaggi
     */
    @GetMapping
    public ResponseEntity<List<Viaggio>> getAllViaggi() {
        return ResponseEntity.ok(viaggioService.getAllViaggi());
    }

    /**
     * Ottiene un viaggio per ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Viaggio> getViaggioById(@PathVariable Long id) {
        Viaggio viaggio = viaggioService.getViaggioById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Viaggio", id));
        return ResponseEntity.ok(viaggio);
    }

    /**
     * Ottiene viaggi per stato
     */
    @GetMapping("/stato/{stato}")
    public ResponseEntity<List<Viaggio>> getViaggiByStato(@PathVariable StatoViaggio stato) {
        return ResponseEntity.ok(viaggioService.getViaggiByStato(stato));
    }

    /**
     * Ottiene viaggi per destinazione
     */
    @GetMapping("/destinazione/{destinazione}")
    public ResponseEntity<List<Viaggio>> getViaggiByDestinazione(@PathVariable String destinazione) {
        return ResponseEntity.ok(viaggioService.getViaggiByDestinazione(destinazione));
    }

    /**
     * Aggiorna completamente un viaggio
     */
    @PutMapping("/{id}")
    public ResponseEntity<Viaggio> updateViaggio(
            @PathVariable Long id,
            @Valid @RequestBody Viaggio viaggio) {
        Viaggio updated = viaggioService.updateViaggio(id, viaggio);
        return ResponseEntity.ok(updated);
    }

    /**
     * Aggiorna parzialmente un viaggio
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Viaggio> patchViaggio(
            @PathVariable Long id,
            @RequestBody Viaggio viaggio) {
        Viaggio updated = viaggioService.patchViaggio(id, viaggio);
        return ResponseEntity.ok(updated);
    }

    /**
     * Aggiorna solo lo stato di un viaggio
     */
    @PatchMapping("/{id}/stato")
    public ResponseEntity<Viaggio> updateStatoViaggio(
            @PathVariable Long id,
            @Valid @RequestBody StatoViaggioUpdateDTO statoDTO) {
        Viaggio updated = viaggioService.updateStatoViaggio(id, statoDTO.getStato());
        return ResponseEntity.ok(updated);
    }

    /**
     * Elimina un viaggio
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViaggio(@PathVariable Long id) {
        viaggioService.deleteViaggio(id);
        return ResponseEntity.noContent().build();
    }
}
