package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "hojas_membreteadas")
@Getter
@Setter
public class HojasMembreteadas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private int cantidad;

    @Column(nullable = false)
    private int cantidadHojas;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_hojas_membreteadas")
    private MedidaHojasMembreteadas medidaHojasMembreteadas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_color_hojas_membreteadas")
    private TipoColorHojasMembreteadas tipoColorHojasMembreteadas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_hojas_membreteadas")
    private CantidadHojasMembreteadas cantidadHojasMembreteadas;
}
