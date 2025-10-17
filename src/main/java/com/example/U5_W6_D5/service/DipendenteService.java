package com.example.U5_W6_D5.service;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.exception.ConflictException;
import com.example.U5_W6_D5.exception.ResourceNotFoundException;
import com.example.U5_W6_D5.repository.DipendenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    // CREATE - Crea un nuovo dipendente
    public Dipendente createDipendente(Dipendente dipendente) {
        // Verifica che username ed email non siano già utilizzati
        if (dipendenteRepository.existsByUsername(dipendente.getUsername())) {
            throw new ConflictException("Username già esistente: " + dipendente.getUsername());
        }
        if (dipendenteRepository.existsByEmail(dipendente.getEmail())) {
            throw new ConflictException("Email già esistente: " + dipendente.getEmail());
        }
        return dipendenteRepository.save(dipendente);
    }

    // READ - Ottieni tutti i dipendenti
    public List<Dipendente> getAllDipendenti() {
        return dipendenteRepository.findAll();
    }

    // READ - Ottieni un dipendente per ID
    public Optional<Dipendente> getDipendenteById(Long id) {
        return dipendenteRepository.findById(id);
    }

    // READ - Ottieni un dipendente per username
    public Optional<Dipendente> getDipendenteByUsername(String username) {
        return dipendenteRepository.findByUsername(username);
    }

    // READ - Ottieni un dipendente per email
    public Optional<Dipendente> getDipendenteByEmail(String email) {
        return dipendenteRepository.findByEmail(email);
    }

    // UPDATE - Aggiorna un dipendente esistente
    public Dipendente updateDipendente(Long id, Dipendente dipendenteAggiornato) {
        return dipendenteRepository.findById(id)
                .map(dipendente -> {
                    // Verifica che il nuovo username non sia già utilizzato da un altro dipendente
                    if (!dipendente.getUsername().equals(dipendenteAggiornato.getUsername()) &&
                            dipendenteRepository.existsByUsername(dipendenteAggiornato.getUsername())) {
                        throw new ConflictException("Username già esistente: " + dipendenteAggiornato.getUsername());
                    }
                    // Verifica che la nuova email non sia già utilizzata da un altro dipendente
                    if (!dipendente.getEmail().equals(dipendenteAggiornato.getEmail()) &&
                            dipendenteRepository.existsByEmail(dipendenteAggiornato.getEmail())) {
                        throw new ConflictException("Email già esistente: " + dipendenteAggiornato.getEmail());
                    }

                    dipendente.setUsername(dipendenteAggiornato.getUsername());
                    dipendente.setNome(dipendenteAggiornato.getNome());
                    dipendente.setCognome(dipendenteAggiornato.getCognome());
                    dipendente.setEmail(dipendenteAggiornato.getEmail());
                    dipendente.setImmagineProfiloPath(dipendenteAggiornato.getImmagineProfiloPath());

                    return dipendenteRepository.save(dipendente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente", id));
    }

    // UPDATE - Aggiorna parzialmente un dipendente
    public Dipendente patchDipendente(Long id, Dipendente dipendenteAggiornato) {
        return dipendenteRepository.findById(id)
                .map(dipendente -> {
                    if (dipendenteAggiornato.getUsername() != null) {
                        if (!dipendente.getUsername().equals(dipendenteAggiornato.getUsername()) &&
                                dipendenteRepository.existsByUsername(dipendenteAggiornato.getUsername())) {
                            throw new ConflictException("Username già esistente: " + dipendenteAggiornato.getUsername());
                        }
                        dipendente.setUsername(dipendenteAggiornato.getUsername());
                    }
                    if (dipendenteAggiornato.getNome() != null) {
                        dipendente.setNome(dipendenteAggiornato.getNome());
                    }
                    if (dipendenteAggiornato.getCognome() != null) {
                        dipendente.setCognome(dipendenteAggiornato.getCognome());
                    }
                    if (dipendenteAggiornato.getEmail() != null) {
                        if (!dipendente.getEmail().equals(dipendenteAggiornato.getEmail()) &&
                                dipendenteRepository.existsByEmail(dipendenteAggiornato.getEmail())) {
                            throw new ConflictException("Email già esistente: " + dipendenteAggiornato.getEmail());
                        }
                        dipendente.setEmail(dipendenteAggiornato.getEmail());
                    }
                    if (dipendenteAggiornato.getImmagineProfiloPath() != null) {
                        dipendente.setImmagineProfiloPath(dipendenteAggiornato.getImmagineProfiloPath());
                    }

                    return dipendenteRepository.save(dipendente);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Dipendente", id));
    }

    // DELETE - Elimina un dipendente per ID
    public void deleteDipendente(Long id) {
        if (!dipendenteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Dipendente", id);
        }
        dipendenteRepository.deleteById(id);
    }

    // Verifica se un dipendente esiste
    public boolean existsById(Long id) {
        return dipendenteRepository.existsById(id);
    }
}
