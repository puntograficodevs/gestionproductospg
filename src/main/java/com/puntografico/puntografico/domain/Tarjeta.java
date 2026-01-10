package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "tarjeta")
@Getter
@Setter
public class Tarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_tarjeta")
    private TipoPapelTarjeta tipoPapelTarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_tarjeta")
    private TipoColorTarjeta tipoColorTarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_tarjeta")
    private TipoFazTarjeta tipoFazTarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_laminado_tarjeta")
    private TipoLaminadoTarjeta tipoLaminadoTarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_tarjeta")
    private MedidaTarjeta medidaTarjeta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_tarjeta")
    private CantidadTarjeta cantidadTarjeta;
}
