package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sello_automatico_escolar")
@Getter
@Setter
public class SelloAutomaticoEscolar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String textoLineaUno;

    private String textoLineaDos;

    private String textoLineaTres;

    @Column(nullable = false)
    private String tipografia;

    @Column(nullable = false)
    private String dibujo;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_sello_automatico_escolar")
    private ModeloSelloAutomaticoEscolar modeloSelloAutomaticoEscolar;
}
