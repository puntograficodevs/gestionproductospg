package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_sello_automatico")
@Getter @Setter
public class PlantillaSelloAutomatico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_sello_automatico")
    private ModeloSelloAutomatico modeloSelloAutomatico;
}
