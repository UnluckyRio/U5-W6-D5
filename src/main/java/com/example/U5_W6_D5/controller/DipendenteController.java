package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.exception.ResourceNotFoundException;
import com.example.U5_W6_D5.service.DipendenteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller REST per la gestione dei dipendenti
 */
@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    /**
     * CREATE - Crea un nuovo dipendente
     * POST /api/dipendenti
     */
    @PostMapping
    public ResponseEntity<Dipendente> createDipendente(@Valid @RequestBody Dipendente dipendente) {
        Dipendente nuovoDipendente = dipendenteService.createDipendente(dipendente);
        return new ResponseEntity<>(nuovoDipendente, HttpStatus.CREATED);
    }

    /**
     * READ - Ottieni tutti i dipendenti
     * GET /api/dipendenti
     */
    @GetMapping
    public ResponseEntity<List<Dipendente>> getAllDipendenti() {
        List<Dipendente> dipendenti = dipendenteService.getAllDipendenti();
        return new ResponseEntity<>(dipendenti, HttpStatus.OK);
    }

    /**
     * READ - Ottieni un dipendente per ID
     * GET /api/dipendenti/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Dipendente> getDipendenteById(@PathVariable Long id) {
        Dipendente dipendente = dipendenteService.getDipendenteById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente", id));
        return ResponseEntity.ok(dipendente);
    }

    /**
     * READ - Ottieni un dipendente per username
     * GET /api/dipendenti/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Dipendente> getDipendenteByUsername(@PathVariable String username) {
        Dipendente dipendente = dipendenteService.getDipendenteByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente", "username", username));
        return ResponseEntity.ok(dipendente);
    }

    /**
     * UPDATE - Aggiorna completamente un dipendente
     * PUT /api/dipendenti/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dipendente> updateDipendente(
            @PathVariable Long id,
            @Valid @RequestBody Dipendente dipendente) {
        Dipendente dipendenteAggiornato = dipendenteService.updateDipendente(id, dipendente);
        return ResponseEntity.ok(dipendenteAggiornato);
    }

    /**
     * UPDATE - Aggiorna parzialmente un dipendente
     * PATCH /api/dipendenti/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Dipendente> patchDipendente(
            @PathVariable Long id,
            @RequestBody Dipendente dipendente) {
        Dipendente dipendenteAggiornato = dipendenteService.patchDipendente(id, dipendente);
        return ResponseEntity.ok(dipendenteAggiornato);
    }

    /**
     * DELETE - Elimina un dipendente
     * DELETE /api/dipendenti/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDipendente(@PathVariable Long id) {
        dipendenteService.deleteDipendente(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Dipendente eliminato con successo");
        return ResponseEntity.ok(response);
    }

    /**
     * UPLOAD - Carica un'immagine profilo per un dipendente su Cloudinary
     * POST /api/dipendenti/{id}/uploadImage
     */
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImmagine(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            // Carica l'immagine su Cloudinary e aggiorna il dipendente
            Dipendente dipendenteAggiornato = dipendenteService.updateImmagineProfilo(id, file);

            response.put("message", "Immagine caricata con successo su Cloudinary");
            response.put("imageUrl", dipendenteAggiornato.getImmagineProfiloPath());

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (ResourceNotFoundException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            response.put("error", "Errore durante l'upload dell'immagine: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
