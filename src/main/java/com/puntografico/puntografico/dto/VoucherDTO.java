package com.puntografico.puntografico.dto;

import lombok.Data;

@Data
public class VoucherDTO {
    private String tipoPapelPersonalizado;
    private String medidaPersonalizada;
    private String enlaceArchivo;
    private Boolean conAdicionalDisenio;
    private String informacionAdicional;
    private Integer cantidad;
    private Long medidaVoucherId;
    private Long tipoPapelVoucherId;
    private Long tipoFazVoucherId;
    private Long cantidadVoucherId;
}
