package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sello_automatico")
@Getter
@Setter
public class SelloAutomatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean esProfesional;

    private boolean esParticular;

    @Column(nullable = false)
    private String textoLineaUno;

    private String textoLineaDos;

    private String textoLineaTres;

    private String textoLineaCuatro;

    @Column(nullable = false)
    private String tipografiaLineaUno;

    private String enlaceArchivo;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_sello_automatico")
    private ModeloSelloAutomatico modeloSelloAutomatico;
}
