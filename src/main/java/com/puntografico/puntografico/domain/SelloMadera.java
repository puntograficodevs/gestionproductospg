package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sello_madera")
@Getter
@Setter
public class SelloMadera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tamanioPersonalizado;

    private boolean conAdicionalPerilla;

    private String detalleSello;

    private String tipografiaLineaUno;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanio_sello_madera")
    private TamanioSelloMadera tamanioSelloMadera;
}
