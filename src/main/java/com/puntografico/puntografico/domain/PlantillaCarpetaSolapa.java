package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_carpeta_solapa")
@Getter @Setter
public class PlantillaCarpetaSolapa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_laminado_carpeta_solapa")
    private TipoLaminadoCarpetaSolapa tipoLaminadoCarpetaSolapa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_carpeta_solapa")
    private TipoFazCarpetaSolapa tipoFazCarpetaSolapa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_carpeta_solapa")
    private CantidadCarpetaSolapa cantidadCarpetaSolapa;
}
