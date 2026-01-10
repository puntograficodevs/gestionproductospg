package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vinilo_plastico_corrugado")
@Getter
@Setter
public class ViniloPlasticoCorrugado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private boolean conOjales;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_vinilo_plastico_corrugado")
    private MedidaViniloPlasticoCorrugado medidaViniloPlasticoCorrugado;
}
