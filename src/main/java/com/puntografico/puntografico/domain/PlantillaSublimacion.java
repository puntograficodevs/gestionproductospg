package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_sublimacion")
@Getter @Setter
public class PlantillaSublimacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_material_sublimacion")
    private MaterialSublimacion materialSublimacion;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_sublimacion")
    private CantidadSublimacion cantidadSublimacion;
}
