package com.example.U5_W6_D5.entity;

import jakarta.persistence.*;
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

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    @Column(nullable = false)
    private String email;

    @Column(name = "immagine_profilo_path")
    private String immagineProfiloPath;
}

