package com.example.U5_W6_D5.entity;

import jakarta.persistence.*;
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

    @Column(name = "data_richiesta", nullable = false)
    private LocalDate dataRichiesta;

    @Column(name = "note_preferenze", columnDefinition = "TEXT")
    private String notePreferenze;

    @ManyToOne
    @JoinColumn(name = "viaggio_id", nullable = false)
    private Viaggio viaggio;

    @ManyToOne
    @JoinColumn(name = "dipendente_id", nullable = false)
    private Dipendente dipendente;
}
package com.example.U5_W6_D5.entity;

public enum StatoViaggio {
    IN_PROGRAMMA,
    COMPLETATO
}

