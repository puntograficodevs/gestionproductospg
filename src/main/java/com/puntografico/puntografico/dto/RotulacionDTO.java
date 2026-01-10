package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class RotulacionDTO {
    private Boolean esLaminado;
    private String horarioRotulacion;
    private String direccionRotulacion;
    private String medida;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoRotulacionId;
    private Long tipoCorteRotulacionId;
}
