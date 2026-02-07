package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "producto")
@Getter @Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;

    private Integer precioDisenio;

    @Column(columnDefinition = "json")
    private String esquemaConfiguracion;

    @Column(columnDefinition = "TEXT")
    private String iconoSvg;
}
