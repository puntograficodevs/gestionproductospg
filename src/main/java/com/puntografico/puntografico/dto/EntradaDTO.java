package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class EntradaDTO {
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoPapelEntradaId;
    private Long tipoColorEntradaId;
    private Long tipoTroqueladoEntradaId;
    private Long medidaEntradaId;
    private Long cantidadEntradaId;
    private Long numeradoEntradaId;
    private Long terminacionEntradaId;
}
