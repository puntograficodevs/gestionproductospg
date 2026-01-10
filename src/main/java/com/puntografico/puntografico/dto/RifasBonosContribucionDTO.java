package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class RifasBonosContribucionDTO {
    private Boolean conNumerado;
    private String detalleNumerado;
    private Boolean conEncolado;
    private String medida;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoPapelRifaId;
    private Long tipoTroqueladoRifaId;
    private Long tipoColorRifaId;
}
