package com.puntografico.puntografico.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class AnotadorDTO {
    private Integer cantidadHojas;
    private String medida;
    private String tipoTapa;
    private Boolean conAdicionalDisenio;
    private String enlaceArchivo;
    private String informacionAdicional;
    private Integer cantidad;
}
