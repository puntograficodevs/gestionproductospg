package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class CierraBolsasDTO {
    private String medidaPersonalizada;
    private Integer cantidad;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Long tipoTroqueladoCierraBolsasId;
    private Long medidaCierraBolsasId;
    private Long cantidadCierraBolsasId;
}
