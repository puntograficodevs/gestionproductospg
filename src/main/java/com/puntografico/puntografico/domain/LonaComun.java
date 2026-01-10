package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "lona_comun")
@Getter
@Setter
public class LonaComun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private boolean conOjales;

    private boolean conOjalesConRefuerzo;

    private boolean conBolsillos;

    private boolean conDemasiaParaTensado;

    private boolean conSolapado;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_lona_comun")
    private MedidaLonaComun medidaLonaComun;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_lona_comun")
    private TipoLonaComun tipoLonaComun;
}
