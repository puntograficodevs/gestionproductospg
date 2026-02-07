package com.puntografico.puntografico.domain;

import lombok.Data;

import java.util.Map;

@Data
public class ConsultaPrecioDTO {
    private Integer productoId;
    private Map<String, String> detalles;
}
