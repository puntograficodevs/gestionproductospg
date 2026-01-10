package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class ComboDTO {
    private Integer cantidad;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Long tipoComboId;
}
