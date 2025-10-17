package com.example.U5_W6_D5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "La data di richiesta è obbligatoria")
    @PastOrPresent(message = "La data di richiesta non può essere nel futuro")
    @Column(name = "data_richiesta", nullable = false)
    private LocalDate dataRichiesta;

    @Size(max = 1000, message = "Le note non possono superare i 1000 caratteri")
    @Column(name = "note_preferenze", columnDefinition = "TEXT")
    private String notePreferenze;

    @NotNull(message = "Il viaggio è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @NotNull(message = "Il dipendente è obbligatorio")
    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;
}
