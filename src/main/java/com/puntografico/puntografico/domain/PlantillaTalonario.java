package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_talonario")
@Getter @Setter
public class PlantillaTalonario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    private int cantidadHojas;

    private boolean conNumerado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_talonario")
    private TipoTalonario tipoTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_troquelado_talonario")
    private TipoTroqueladoTalonario tipoTroqueladoTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modo_talonario")
    private ModoTalonario modoTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_talonario")
    private TipoColorTalonario tipoColorTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_talonario")
    private MedidaTalonario medidaTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_talonario")
    private TipoPapelTalonario tipoPapelTalonario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_talonario")
    private CantidadTalonario cantidadTalonario;

}
