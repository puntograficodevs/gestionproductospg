package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_lona_publicitaria")
@Getter @Setter
public class PlantillaLonaPublicitaria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean conAdicionalPortabanner;

    private boolean conOjales;

    private boolean conOjalesConRefuerzo;

    private boolean conBolsillos;

    private boolean conDemasiaParaTensado;

    private boolean conSolapado;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_lona_publicitaria")
    private MedidaLonaPublicitaria medidaLonaPublicitaria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_lona_publicitaria")
    private TipoLonaPublicitaria tipoLonaPublicitaria;
}
