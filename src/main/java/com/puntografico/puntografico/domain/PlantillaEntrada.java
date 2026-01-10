package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "plantilla_entrada")
@Getter @Setter
public class PlantillaEntrada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_entrada")
    private TipoPapelEntrada tipoPapelEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_entrada")
    private TipoColorEntrada tipoColorEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_troquelado_entrada")
    private TipoTroqueladoEntrada tipoTroqueladoEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_entrada")
    private MedidaEntrada medidaEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_entrada")
    private CantidadEntrada cantidadEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_numerado_entrada")
    private NumeradoEntrada numeradoEntrada;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_terminacion_entrada")
    private TerminacionEntrada terminacionEntrada;
}
