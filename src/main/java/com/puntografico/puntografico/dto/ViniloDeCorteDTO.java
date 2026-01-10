package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class ViniloDeCorteDTO {
    private Boolean esPromocional;
    private Boolean esOracal;
    private String codigoColor;
    private Boolean conColocacion;
    private String medida;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long traeMaterialViniloId;
}
