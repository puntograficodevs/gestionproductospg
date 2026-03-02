package com.puntografico.puntografico.domain;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    @Transient // Esto indica que no es una columna de la base de datos
    public List<Map<String, Object>> getCamposList() {
        if (this.esquemaConfiguracion == null || this.esquemaConfiguracion.isEmpty()) {
            return Collections.emptyList();
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.esquemaConfiguracion, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}
