package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class ImpresionDTO {
    private Boolean esAnillado;
    private String enlaceArchivo;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoColorImpresionId;
    private Long tamanioHojaImpresionId;
    private Long tipoFazImpresionId;
    private Long tipoPapelImpresionId;
    private Long cantidadImpresionId;
    private Long tipoImpresionId;
}
