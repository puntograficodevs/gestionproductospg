package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_goma_polimero")
@Getter
@Setter
public class PlantillaGomaPolimero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_modelo_goma_polimero")
    private ModeloGomaPolimero modeloGomaPolimero;
}
