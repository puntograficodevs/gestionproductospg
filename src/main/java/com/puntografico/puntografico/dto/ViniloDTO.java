package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class ViniloDTO {
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoViniloId;
    private Long tipoAdicionalViniloId;
    private Long tipoCorteViniloId;
    private Long medidaViniloId;
    private Long cantidadViniloId;
}
