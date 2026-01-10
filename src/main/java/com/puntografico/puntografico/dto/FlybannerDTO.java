package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class FlybannerDTO {
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoFazFlybannerId;
    private Long alturaFlybannerId;
    private Long banderaFlybannerId;
    private Long tipoBaseFlybannerId;
}
