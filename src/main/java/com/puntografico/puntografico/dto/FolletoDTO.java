package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class FolletoDTO {
    private Boolean conPlegado;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoPapelFolletoId;
    private Long tipoColorFolletoId;
    private Long tipoFazFolletoId;
    private Long tamanioHojaFolletoId;
    private Long tipoFolletoId;
    private Long cantidadFolletoId;
}
