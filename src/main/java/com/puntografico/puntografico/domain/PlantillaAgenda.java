package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name="plantilla_agenda")
@Getter @Setter
public class PlantillaAgenda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    private int cantidadHojas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_tapa_agenda")
    private TipoTapaAgenda tipoTapaAgenda;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_agenda")
    private TipoColorAgenda tipoColorAgenda;
}
