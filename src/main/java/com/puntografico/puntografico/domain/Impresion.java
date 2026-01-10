package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "impresion")
@Getter
@Setter
public class Impresion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean esAnillado;

    private String enlaceArchivo;

    private String informacionAdicional;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_impresion")
    private TipoColorImpresion tipoColorImpresion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanio_hoja_impresion")
    private TamanioHojaImpresion tamanioHojaImpresion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_impresion")
    private TipoFazImpresion tipoFazImpresion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_impresion")
    private TipoPapelImpresion tipoPapelImpresion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_impresion")
    private CantidadImpresion cantidadImpresion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_impresion")
    private TipoImpresion tipoImpresion;
}
