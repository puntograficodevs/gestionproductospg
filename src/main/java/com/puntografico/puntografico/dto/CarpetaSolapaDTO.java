package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class CarpetaSolapaDTO {
    private String tipoPapel;
    private Integer cantidad;
    private Long tipoLaminadoCarpetaSolapaId;
    private Long tipoFazCarpetaSolapaId;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
}
