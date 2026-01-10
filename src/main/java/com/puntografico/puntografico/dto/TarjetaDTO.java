package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class TarjetaDTO {
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoPapelTarjetaId;
    private Long tipoColorTarjetaId;
    private Long tipoFazTarjetaId;
    private Long tipoLaminadoTarjetaId;
    private Long medidaTarjetaId;
    private Long cantidadTarjetaId;
}
