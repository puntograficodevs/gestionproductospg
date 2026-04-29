package com.puntografico.puntografico.dto;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.TipoMovimiento;
import lombok.Data;

@Data
public class MovimientoDTO {
    private TipoMovimiento tipoMovimiento;
    private String correccion;
    private String usernameAsociado;
}
