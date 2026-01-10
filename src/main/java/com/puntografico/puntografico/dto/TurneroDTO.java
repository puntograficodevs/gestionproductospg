package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class TurneroDTO {
    private String medidaPersonalizada;
    private Integer cantidad;
    private Integer cantidadHojas;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Long tipoColorTurneroId;
    private Long cantidadTurneroId;
    private Long medidaTurneroId;
}
