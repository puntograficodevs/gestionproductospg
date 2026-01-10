package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "folleto")
@Getter @Setter
public class Folleto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean conPlegado;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_folleto")
    private TipoPapelFolleto tipoPapelFolleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_folleto")
    private TipoColorFolleto tipoColorFolleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_folleto")
    private TipoFazFolleto tipoFazFolleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanio_hoja_folleto")
    private TamanioHojaFolleto tamanioHojaFolleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_folleto")
    private TipoFolleto tipoFolleto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_folleto")
    private CantidadFolleto cantidadFolleto;
}
