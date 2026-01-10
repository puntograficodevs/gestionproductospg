package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class TalonarioDTO {
    private Boolean conNumerado;
    private Integer cantidadHojas;
    private String detalleNumerado;
    private Boolean esEncolado;
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoTalonarioId;
    private Long tipoTroqueladoTalonarioId;
    private Long modoTalonarioId;
    private Long tipoColorTalonarioId;
    private Long medidaTalonarioId;
    private Long tipoPapelTalonarioId;
    private Long cantidadTalonarioId;
}
