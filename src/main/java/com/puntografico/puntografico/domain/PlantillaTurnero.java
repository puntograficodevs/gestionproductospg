package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_turnero")
@Getter
@Setter
public class PlantillaTurnero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    private Integer cantidadHojas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_turnero")
    private TipoColorTurnero tipoColorTurnero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_turnero")
    private CantidadTurnero cantidadTurnero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_turnero")
    private MedidaTurnero medidaTurnero;
}
