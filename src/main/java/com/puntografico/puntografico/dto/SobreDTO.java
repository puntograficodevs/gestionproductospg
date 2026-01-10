package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class SobreDTO {
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long medidaSobreId;
    private Long tipoColorSobreId;
    private Long cantidadSobreId;
}
