package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_impresion")
@Getter @Setter
public class PlantillaImpresion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

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
