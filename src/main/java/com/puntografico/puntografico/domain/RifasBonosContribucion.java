package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "rifas_bonos_contribucion")
@Getter
@Setter
public class RifasBonosContribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean conNumerado;

    private String detalleNumerado;

    private boolean conEncolado;

    private String medida;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_rifa")
    private TipoPapelRifa tipoPapelRifa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_troquelado_rifa")
    private TipoTroqueladoRifa tipoTroqueladoRifa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_rifa")
    private TipoColorRifa tipoColorRifa;
}
