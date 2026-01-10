package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class SelloAutomaticoEscolarDTO {
    private String textoLineaUno;
    private String textoLineaDos;
    private String textoLineaTres;
    private String tipografia;
    private String dibujo;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long modeloSelloAutomaticoEscolarId;
}
