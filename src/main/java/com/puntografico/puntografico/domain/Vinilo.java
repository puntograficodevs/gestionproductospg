package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vinilo")
@Getter
@Setter
public class Vinilo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_vinilo")
    private TipoVinilo tipoVinilo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_adicional_vinilo")
    private TipoAdicionalVinilo tipoAdicionalVinilo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_corte_vinilo")
    private TipoCorteVinilo tipoCorteVinilo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_vinilo")
    private MedidaVinilo medidaVinilo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_vinilo")
    private CantidadVinilo cantidadVinilo;
}
