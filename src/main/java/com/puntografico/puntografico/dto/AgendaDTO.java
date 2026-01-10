package com.puntografico.puntografico.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter @Setter
public class AgendaDTO {
    private Integer cantidadHojas;
    private Long tipoTapaAgendaId;
    private Long tipoColorAgendaId;
    private Integer cantidad;
    private String medida;
    private String tipoTapaPersonalizada;
    private Boolean conAdicionalDisenio;
    private String enlaceArchivo;
    private String informacionAdicional;
}
