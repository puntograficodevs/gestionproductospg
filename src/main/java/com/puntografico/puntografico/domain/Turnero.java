package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "turnero")
@Getter
@Setter
public class Turnero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private int cantidad;

    @Column(nullable = false)
    private int cantidadHojas;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

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
