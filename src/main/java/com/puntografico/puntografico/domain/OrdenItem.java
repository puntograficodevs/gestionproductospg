package com.puntografico.puntografico.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "orden_item")
@Getter @Setter
public class OrdenItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orden_id")
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "producto_id")
    private Producto producto;

    private int cantidad;

    private int precioUnitario;

    private boolean conAdicionalDisenio;

    private int precioDisenio;

    @Column(columnDefinition = "json")
    private String detallePersonalizado;

    @Transient
    private Map<String, String> detalles = new HashMap<>();

    public Map<String, String> getDetallesMap() {
        if (this.detallePersonalizado == null || this.detallePersonalizado.isEmpty()) {
            return new HashMap<>();
        }
        try {
            return new ObjectMapper().readValue(this.detallePersonalizado, new MapTypeReference());
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    // Clase interna rápida para Jackson
    private static class MapTypeReference extends com.fasterxml.jackson.core.type.TypeReference<Map<String, String>> {}
}
