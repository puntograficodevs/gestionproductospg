package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rotulacion")
@Getter @Setter
public class Rotulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean esLaminado;

    private String horarioRotulacion;

    private String direccionRotulacion;

    private String medida;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch =  FetchType.EAGER)
    @JoinColumn(name = "id_tipo_rotulacion")
    private TipoRotulacion tipoRotulacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_corte_rotulacion")
    private TipoCorteRotulacion tipoCorteRotulacion;
}
