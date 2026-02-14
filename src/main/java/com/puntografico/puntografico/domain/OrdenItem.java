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

    @Column(columnDefinition = "json")
    private String detallePersonalizado;
 
    public Map<String, Object> getDetallesMap() {
        try {
            if (this.detallePersonalizado == null || this.detallePersonalizado.isEmpty()) {
                return new HashMap<>();
            }
            return new ObjectMapper().readValue(this.detallePersonalizado, new TypeReference<Map<String, Object>>() {});
        } catch (Exception e) {
            return new HashMap<>();
        }
    }
}
