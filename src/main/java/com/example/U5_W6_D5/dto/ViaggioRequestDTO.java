package com.example.U5_W6_D5.dto;

import com.example.U5_W6_D5.entity.StatoViaggio;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO per la creazione/aggiornamento di un viaggio
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ViaggioRequestDTO {

    @NotBlank(message = "La destinazione è obbligatoria")
    @Size(min = 2, max = 200, message = "La destinazione deve essere tra 2 e 200 caratteri")
    private String destinazione;

    @NotNull(message = "La data è obbligatoria")
    @FutureOrPresent(message = "La data del viaggio non può essere nel passato")
    private LocalDate data;

    @NotNull(message = "Lo stato del viaggio è obbligatorio")
    private StatoViaggio stato;
}
