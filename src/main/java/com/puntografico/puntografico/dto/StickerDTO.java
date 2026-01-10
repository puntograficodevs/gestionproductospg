package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class StickerDTO {
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoTroqueladoStickerId;
    private Long cantidadStickerId;
    private Long medidaStickerId;
}
