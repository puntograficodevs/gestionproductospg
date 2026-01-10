package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_lona_comun")
@Getter @Setter
public class PlantillaLonaComun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    private boolean conBolsillos;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_lona_comun")
    private MedidaLonaComun medidaLonaComun;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_lona_comun")
    private TipoLonaComun tipoLonaComun;
}
