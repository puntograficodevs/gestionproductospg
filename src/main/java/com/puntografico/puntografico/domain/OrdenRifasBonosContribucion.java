package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "orden_rifas_bonos_contribucion")
@Getter @Setter
public class OrdenRifasBonosContribucion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_orden_trabajo")
    private OrdenTrabajo ordenTrabajo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rifas_bonos_contribucion")
    private RifasBonosContribucion rifasBonosContribucion;
}
