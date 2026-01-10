package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "anotador")
@Getter @Setter
public class Anotador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medida;

    @Column(nullable = false)
    private String tipoTapa;

    @Column(nullable = false)
    private int cantidadHojas;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;
}
