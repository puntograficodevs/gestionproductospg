package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sticker")
@Getter
@Setter
public class Sticker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_troquelado_sticker")
    private TipoTroqueladoSticker tipoTroqueladoSticker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_sticker")
    private CantidadSticker cantidadSticker;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_sticker")
    private MedidaSticker medidaSticker;
}
