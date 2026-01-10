package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "agenda")
@Getter @Setter
public class Agenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String medida;

    private String tipoTapaPersonalizada;

    @Column(nullable = false)
    private int cantidadHojas;

    private int cantidad;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_tapa_agenda")
    private TipoTapaAgenda tipoTapaAgenda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_agenda")
    private TipoColorAgenda tipoColorAgenda;
}
