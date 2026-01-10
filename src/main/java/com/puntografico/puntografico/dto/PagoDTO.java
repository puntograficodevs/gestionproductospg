package com.puntografico.puntografico.dto;

import com.puntografico.puntografico.domain.Empleado;
import com.puntografico.puntografico.domain.MedioPago;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PagoDTO {
    private int importe;
    private LocalDate fechaPago;
    private MedioPago medioPago;
    private Empleado empleado;
}
