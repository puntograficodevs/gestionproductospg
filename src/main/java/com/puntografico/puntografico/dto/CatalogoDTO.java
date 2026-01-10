package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class CatalogoDTO {
    private String tipoPapel;
    private Integer cantidad;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Long tipoLaminadoCatalogoId;
    private Long tipoFazCatalogoId;
}
