package com.example.U5_W6_D5.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO per la creazione di una nuova prenotazione
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneRequestDTO {

    @NotNull(message = "L'ID del dipendente è obbligatorio")
    private Long dipendenteId;

    @NotNull(message = "L'ID del viaggio è obbligatorio")
    private Long viaggioId;

    private String notePreferenze;
}

