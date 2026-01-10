package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class SelloAutomaticoDTO {
    private Boolean esProfesional;
    private Boolean esParticular;
    private String textoLineaUno;
    private String textoLineaDos;
    private String textoLineaTres;
    private String textoLineaCuatro;
    private String tipografiaLineaUno;
    private String enlaceArchivo;
    private String informacionAdicional;
    private Integer cantidad;
    private Long modeloSelloAutomaticoId;
}
