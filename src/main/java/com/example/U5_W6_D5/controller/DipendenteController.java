package com.example.U5_W6_D5.controller;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.service.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Controller REST per la gestione dei dipendenti
 */
@RestController
@RequestMapping("/api/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;

    @Value("${upload.path:uploads/dipendenti}")
    private String uploadPath;

    /**
     * CREATE - Crea un nuovo dipendente
     * POST /api/dipendenti
     */
    @PostMapping
    public ResponseEntity<Dipendente> createDipendente(@RequestBody Dipendente dipendente) {
        try {
            Dipendente nuovoDipendente = dipendenteService.createDipendente(dipendente);
            return new ResponseEntity<>(nuovoDipendente, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
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
        return dipendenteService.getDipendenteById(id)
                .map(dipendente -> new ResponseEntity<>(dipendente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * READ - Ottieni un dipendente per username
     * GET /api/dipendenti/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<Dipendente> getDipendenteByUsername(@PathVariable String username) {
        return dipendenteService.getDipendenteByUsername(username)
                .map(dipendente -> new ResponseEntity<>(dipendente, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * UPDATE - Aggiorna completamente un dipendente
     * PUT /api/dipendenti/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<Dipendente> updateDipendente(
            @PathVariable Long id,
            @RequestBody Dipendente dipendente) {
        try {
            Dipendente dipendenteAggiornato = dipendenteService.updateDipendente(id, dipendente);
            return new ResponseEntity<>(dipendenteAggiornato, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * UPDATE - Aggiorna parzialmente un dipendente
     * PATCH /api/dipendenti/{id}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<Dipendente> patchDipendente(
            @PathVariable Long id,
            @RequestBody Dipendente dipendente) {
        try {
            Dipendente dipendenteAggiornato = dipendenteService.patchDipendente(id, dipendente);
            return new ResponseEntity<>(dipendenteAggiornato, HttpStatus.OK);
        } catch (RuntimeException e) {
            if (e instanceof IllegalArgumentException) {
                return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
            }
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * DELETE - Elimina un dipendente
     * DELETE /api/dipendenti/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteDipendente(@PathVariable Long id) {
        try {
            dipendenteService.deleteDipendente(id);
            Map<String, String> response = new HashMap<>();
            response.put("message", "Dipendente eliminato con successo");
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * UPLOAD - Carica un'immagine profilo per un dipendente
     * POST /api/dipendenti/{id}/uploadImage
     */
    @PostMapping("/{id}/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImmagine(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        Map<String, String> response = new HashMap<>();

        try {
            // Verifica che il dipendente esista
            Dipendente dipendente = dipendenteService.getDipendenteById(id)
                    .orElseThrow(() -> new RuntimeException("Dipendente non trovato con ID: " + id));

            // Validazione del file
            if (file.isEmpty()) {
                response.put("error", "Il file è vuoto");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Verifica che sia un'immagine
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                response.put("error", "Il file deve essere un'immagine");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // Crea la directory se non esiste
            Path uploadDir = Paths.get(uploadPath);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Genera un nome file univoco
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = "dipendente_" + id + "_" + UUID.randomUUID().toString() + fileExtension;

            // Salva il file
            Path filePath = uploadDir.resolve(filename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Aggiorna il percorso dell'immagine nel database
            String imagePath = uploadPath + "/" + filename;
            dipendente.setImmagineProfiloPath(imagePath);
            dipendenteService.patchDipendente(id, dipendente);

            response.put("message", "Immagine caricata con successo");
            response.put("filename", filename);
            response.put("path", imagePath);

            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (RuntimeException e) {
            response.put("error", e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            response.put("error", "Errore durante il salvataggio del file: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
