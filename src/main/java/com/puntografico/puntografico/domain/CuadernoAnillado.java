package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "cuaderno_anillado")
@Getter @Setter
public class CuadernoAnillado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String medidaPersonalizada;

    private String tipoTapaPersonalizada;

    @Column(nullable = false)
    private int cantidadHojas;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    @Column(nullable = false)
    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_tapa_cuaderno_anillado")
    private TipoTapaCuadernoAnillado tipoTapaCuadernoAnillado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_cuaderno_anillado")
    private MedidaCuadernoAnillado medidaCuadernoAnillado;
}
