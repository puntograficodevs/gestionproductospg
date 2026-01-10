package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "carpeta_solapa")
@Getter @Setter
public class CarpetaSolapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String tipoPapel;

    @Column(nullable = false)
    private int cantidad;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_laminado_carpeta_solapa")
    private TipoLaminadoCarpetaSolapa tipoLaminadoCarpetaSolapa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_carpeta_solapa")
    private TipoFazCarpetaSolapa tipoFazCarpetaSolapa;
}
