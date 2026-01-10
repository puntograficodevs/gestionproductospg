package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_sello_madera")
@Getter
@Setter
public class PlantillaSelloMadera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tamanio_sello_madera")
    private TamanioSelloMadera tamanioSelloMadera;

}
