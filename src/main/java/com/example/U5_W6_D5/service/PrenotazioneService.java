package com.example.U5_W6_D5.service;

import com.example.U5_W6_D5.entity.Dipendente;
import com.example.U5_W6_D5.entity.Prenotazione;
import com.example.U5_W6_D5.entity.Viaggio;
import com.example.U5_W6_D5.repository.DipendenteRepository;
import com.example.U5_W6_D5.repository.PrenotazioneRepository;
import com.example.U5_W6_D5.repository.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private ViaggioRepository viaggioRepository;

    /**
     * Crea una nuova prenotazione con validazione per evitare sovrapposizioni.
     * Verifica che il dipendente non abbia già prenotazioni per la stessa data del viaggio.
     *
     * @param dipendenteId   ID del dipendente
     * @param viaggioId      ID del viaggio
     * @param notePreferenze Note o preferenze del dipendente (opzionale)
     * @return La prenotazione creata
     * @throws IllegalArgumentException se i parametri non sono validi
     * @throws IllegalStateException    se il dipendente ha già una prenotazione per quella data
     * @throws RuntimeException         se dipendente o viaggio non esistono
     */
    @Transactional
    public Prenotazione createPrenotazione(Long dipendenteId, Long viaggioId, String notePreferenze) {
        // Validazione input
        if (dipendenteId == null || viaggioId == null) {
            throw new IllegalArgumentException("ID dipendente e ID viaggio sono obbligatori");
        }

        // Recupera il dipendente
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato con ID: " + dipendenteId));

        // Recupera il viaggio
        Viaggio viaggio = viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + viaggioId));

        // VALIDAZIONE SOVRAPPOSIZIONE: Verifica che il dipendente non abbia altre prenotazioni per la stessa data
        LocalDate dataViaggio = viaggio.getData();
        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository
                .findByDipendenteIdAndViaggioData(dipendenteId, dataViaggio);

        if (!prenotazioniEsistenti.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Il dipendente %s %s ha già una prenotazione per la data %s",
                            dipendente.getNome(), dipendente.getCognome(), dataViaggio)
            );
        }

        // Crea la nuova prenotazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);
        prenotazione.setDataRichiesta(LocalDate.now());
        prenotazione.setNotePreferenze(notePreferenze);

        return prenotazioneRepository.save(prenotazione);
    }

    /**
     * Crea una nuova prenotazione passando l'oggetto Prenotazione.
     * Verifica che il dipendente non abbia già prenotazioni per la stessa data del viaggio.
     *
     * @param prenotazione L'oggetto Prenotazione da salvare
     * @return La prenotazione creata
     * @throws IllegalArgumentException se i parametri non sono validi
     * @throws IllegalStateException    se il dipendente ha già una prenotazione per quella data
     */
    @Transactional
    public Prenotazione createPrenotazione(Prenotazione prenotazione) {
        // Validazione input
        if (prenotazione == null) {
            throw new IllegalArgumentException("La prenotazione non può essere null");
        }
        if (prenotazione.getDipendente() == null || prenotazione.getDipendente().getId() == null) {
            throw new IllegalArgumentException("Il dipendente è obbligatorio");
        }
        if (prenotazione.getViaggio() == null || prenotazione.getViaggio().getId() == null) {
            throw new IllegalArgumentException("Il viaggio è obbligatorio");
        }

        Long dipendenteId = prenotazione.getDipendente().getId();
        Long viaggioId = prenotazione.getViaggio().getId();

        // Verifica che dipendente e viaggio esistano
        Dipendente dipendente = dipendenteRepository.findById(dipendenteId)
                .orElseThrow(() -> new RuntimeException("Dipendente non trovato con ID: " + dipendenteId));

        Viaggio viaggio = viaggioRepository.findById(viaggioId)
                .orElseThrow(() -> new RuntimeException("Viaggio non trovato con ID: " + viaggioId));

        // VALIDAZIONE SOVRAPPOSIZIONE
        LocalDate dataViaggio = viaggio.getData();
        List<Prenotazione> prenotazioniEsistenti = prenotazioneRepository
                .findByDipendenteIdAndViaggioData(dipendenteId, dataViaggio);

        if (!prenotazioniEsistenti.isEmpty()) {
            throw new IllegalStateException(
                    String.format("Il dipendente %s %s ha già una prenotazione per la data %s",
                            dipendente.getNome(), dipendente.getCognome(), dataViaggio)
            );
        }

        // Imposta la data di richiesta se non presente
        if (prenotazione.getDataRichiesta() == null) {
            prenotazione.setDataRichiesta(LocalDate.now());
        }

        // Assicurati che gli oggetti completi siano assegnati
        prenotazione.setDipendente(dipendente);
        prenotazione.setViaggio(viaggio);

        return prenotazioneRepository.save(prenotazione);
    }

    /**
     * Ottiene tutte le prenotazioni.
     *
     * @return Lista di tutte le prenotazioni
     */
    public List<Prenotazione> getAllPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    /**
     * Ottiene una prenotazione per ID.
     *
     * @param id ID della prenotazione
     * @return Optional con la prenotazione se trovata
     */
    public Optional<Prenotazione> getPrenotazioneById(Long id) {
        return prenotazioneRepository.findById(id);
    }

    /**
     * Ottiene tutte le prenotazioni di un dipendente.
     *
     * @param dipendenteId ID del dipendente
     * @return Lista delle prenotazioni del dipendente
     */
    public List<Prenotazione> getPrenotazioniByDipendente(Long dipendenteId) {
        return prenotazioneRepository.findByDipendenteId(dipendenteId);
    }

    /**
     * Ottiene tutte le prenotazioni per un viaggio.
     *
     * @param viaggioId ID del viaggio
     * @return Lista delle prenotazioni per il viaggio
     */
    public List<Prenotazione> getPrenotazioniByViaggio(Long viaggioId) {
        return prenotazioneRepository.findByViaggioId(viaggioId);
    }

    /**
     * Verifica se un dipendente ha già una prenotazione per una specifica data.
     *
     * @param dipendenteId ID del dipendente
     * @param data         Data da verificare
     * @return true se esiste già una prenotazione, false altrimenti
     */
    public boolean hasPrenotazioneOnDate(Long dipendenteId, LocalDate data) {
        List<Prenotazione> prenotazioni = prenotazioneRepository
                .findByDipendenteIdAndViaggioData(dipendenteId, data);
        return !prenotazioni.isEmpty();
    }

    /**
     * Aggiorna le note/preferenze di una prenotazione esistente.
     *
     * @param id             ID della prenotazione
     * @param notePreferenze Nuove note o preferenze
     * @return La prenotazione aggiornata
     * @throws RuntimeException se la prenotazione non esiste
     */
    @Transactional
    public Prenotazione updateNotePreferenze(Long id, String notePreferenze) {
        return prenotazioneRepository.findById(id)
                .map(prenotazione -> {
                    prenotazione.setNotePreferenze(notePreferenze);
                    return prenotazioneRepository.save(prenotazione);
                })
                .orElseThrow(() -> new RuntimeException("Prenotazione non trovata con ID: " + id));
    }

    /**
     * Elimina una prenotazione.
     *
     * @param id ID della prenotazione da eliminare
     * @throws RuntimeException se la prenotazione non esiste
     */
    @Transactional
    public void deletePrenotazione(Long id) {
        if (!prenotazioneRepository.existsById(id)) {
            throw new RuntimeException("Prenotazione non trovata con ID: " + id);
        }
        prenotazioneRepository.deleteById(id);
    }

    /**
     * Verifica se una prenotazione esiste.
     *
     * @param id ID della prenotazione
     * @return true se esiste, false altrimenti
     */
    public boolean existsById(Long id) {
        return prenotazioneRepository.existsById(id);
    }
}

