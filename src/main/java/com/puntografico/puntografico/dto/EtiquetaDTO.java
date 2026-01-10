package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class EtiquetaDTO {
    private String medidaPersonalizada;
    private Boolean conPerforacionAdicional;
    private Boolean conMarcaAdicional;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long tipoPapelEtiquetaId;
    private Long tipoLaminadoEtiquetaId;
    private Long tamanioPerforacionId;
    private Long tipoFazEtiquetaId;
    private Long cantidadEtiquetaId;
    private Long medidaEtiquetaId;
}
