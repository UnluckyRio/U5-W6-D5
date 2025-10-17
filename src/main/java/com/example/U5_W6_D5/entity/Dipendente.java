package com.example.U5_W6_D5.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "dipendenti")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class Dipendente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Lo username è obbligatorio")
    @Size(min = 3, max = 50, message = "Lo username deve essere tra 3 e 50 caratteri")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Il nome è obbligatorio")
    @Size(min = 2, max = 100, message = "Il nome deve essere tra 2 e 100 caratteri")
    @Column(nullable = false)
    private String nome;

    @NotBlank(message = "Il cognome è obbligatorio")
    @Size(min = 2, max = 100, message = "Il cognome deve essere tra 2 e 100 caratteri")
    @Column(nullable = false)
    private String cognome;

    @NotBlank(message = "L'email è obbligatoria")
    @Email(message = "L'email deve essere valida")
    @Column(nullable = false)
    private String email;

    @Column(name = "immagine_profilo_path")
    private String immagineProfiloPath;
}
