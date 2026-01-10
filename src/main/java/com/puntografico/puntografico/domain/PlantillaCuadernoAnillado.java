package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_cuaderno_anillado")
@Getter @Setter
public class PlantillaCuadernoAnillado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    private int cantidadHojas;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_tapa_cuaderno_anillado")
    private TipoTapaCuadernoAnillado tipoTapaCuadernoAnillado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_cuaderno_anillado")
    private MedidaCuadernoAnillado medidaCuadernoAnillado;
}
