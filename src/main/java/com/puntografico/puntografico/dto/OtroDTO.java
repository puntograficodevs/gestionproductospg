package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class OtroDTO {
    private String medida;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoColorOtroId;
}
