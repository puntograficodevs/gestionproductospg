package com.puntografico.puntografico.dto;

import lombok.Data;

import java.util.Map;

@Data
public class ConsultaPrecioDTO {
    private Integer productoId;
    private Map<String, String> detalles;
}
