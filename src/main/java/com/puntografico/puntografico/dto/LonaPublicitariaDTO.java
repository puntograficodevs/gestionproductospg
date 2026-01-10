package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class LonaPublicitariaDTO {
    private Boolean conAdicionalPortabanner;
    private Boolean conOjales;
    private Boolean conOjalesConRefuerzo;
    private Boolean conBolsillos;
    private Boolean conDemasiaParaTensado;
    private Boolean conSolapado;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long medidaLonaPublicitariaId;
    private Long tipoLonaPublicitariaId;
}
