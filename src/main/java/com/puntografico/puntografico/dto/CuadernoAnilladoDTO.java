package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class CuadernoAnilladoDTO {
    private String medidaPersonalizada;
    private String tipoTapaPersonalizada;
    private Integer cantidadHojas;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoTapaCuadernoAnilladoId;
    private Long medidaCuadernoAnilladoId;
}
