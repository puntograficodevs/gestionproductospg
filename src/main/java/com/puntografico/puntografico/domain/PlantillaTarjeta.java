package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_tarjeta")
@Getter
@Setter
public class PlantillaTarjeta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

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
