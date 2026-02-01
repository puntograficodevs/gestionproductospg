package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class GomaPolimeroDTO {
    private String enlaceArchivo;
    private String informacionAdicional;
    private Boolean conAdicionalDisenio;
    private Integer cantidad;
    private Long modeloGomaPolimeroId;
}
