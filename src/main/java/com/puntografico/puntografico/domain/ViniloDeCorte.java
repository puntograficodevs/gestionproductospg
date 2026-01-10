package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "vinilo_de_corte")
@Getter
@Setter
public class ViniloDeCorte {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private boolean esPromocional;

    private boolean esOracal;

    private String codigoColor;

    private boolean conColocacion;

    private String medida;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_trae_material_vinilo")
    private TraeMaterialVinilo traeMaterialVinilo;
}
