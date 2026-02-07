package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "producto_catalogo")
@Getter @Setter
public class ProductoCatalogo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreNegocio;

    private Integer precio;

    @Column(columnDefinition = "json")
    private String detallesProducto;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;
}
