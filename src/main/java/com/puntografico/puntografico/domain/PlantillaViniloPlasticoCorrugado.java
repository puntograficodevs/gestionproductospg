package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_vinilo_plastico_corrugado")
@Getter @Setter
public class PlantillaViniloPlasticoCorrugado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_vinilo_plastico_corrugado")
    private MedidaViniloPlasticoCorrugado medidaViniloPlasticoCorrugado;
}
