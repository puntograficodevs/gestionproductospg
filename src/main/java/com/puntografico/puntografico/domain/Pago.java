package com.puntografico.puntografico.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "pago")
@Getter
@Setter
public class Pago {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int importe;

    @Column(nullable = false)
    private LocalDate fechaPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medio_pago")
    private MedioPago medioPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_orden_trabajo")
    @JsonIgnore
    private OrdenTrabajo ordenTrabajo;
}
