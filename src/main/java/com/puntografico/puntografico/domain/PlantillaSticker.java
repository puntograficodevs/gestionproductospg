package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_sticker")
@Getter @Setter
public class PlantillaSticker {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

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
