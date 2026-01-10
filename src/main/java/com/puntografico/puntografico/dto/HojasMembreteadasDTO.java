package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class HojasMembreteadasDTO {
    private String medidaPersonalizada;
    private Integer cantidad;
    private Integer cantidadHojas;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Long medidaHojasMembreteadasId;
    private Long tipoColorHojasMembreteadasId;
    private Long cantidadHojasMembreteadasId;
}
