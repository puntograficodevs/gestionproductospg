package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "goma_polimero")
@Getter
@Setter
public class GomaPolimero {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String enlaceArchivo;

    private String informacionAdicional;

    private boolean conAdicionalDisenio;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_goma_polimero")
    private ModeloGomaPolimero modeloGomaPolimero;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;
}
