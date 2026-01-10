package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class SelloMaderaDTO {
    private String tamanioPersonalizado;
    private Boolean conAdicionalPerilla;
    private String detalleSello;
    private String tipografiaLineaUno;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tamanioSelloMaderaId;
}
