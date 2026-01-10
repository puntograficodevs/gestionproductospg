package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class SublimacionDTO {
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long materialSublimacionId;
    private Long cantidadSublimacionId;
}
