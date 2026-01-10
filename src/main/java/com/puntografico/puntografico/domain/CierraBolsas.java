package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cierra_bolsas")
@Getter @Setter
public class CierraBolsas {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    @Column(nullable = false)
    private int cantidad;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_troquelado_cierra_bolsas")
    private TipoTroqueladoCierraBolsas tipoTroqueladoCierraBolsas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_cierra_bolsas")
    private MedidaCierraBolsas medidaCierraBolsas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_cierra_bolsas")
    private CantidadCierraBolsas cantidadCierraBolsas;
}
