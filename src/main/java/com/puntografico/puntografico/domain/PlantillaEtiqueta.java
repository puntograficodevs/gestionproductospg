package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_etiqueta")
@Getter @Setter
public class PlantillaEtiqueta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_etiqueta")
    private TipoPapelEtiqueta tipoPapelEtiqueta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_laminado_etiqueta")
    private TipoLaminadoEtiqueta tipoLaminadoEtiqueta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_etiqueta")
    private TipoFazEtiqueta tipoFazEtiqueta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_etiqueta")
    private CantidadEtiqueta cantidadEtiqueta;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_etiqueta")
    private MedidaEtiqueta medidaEtiqueta;
}
