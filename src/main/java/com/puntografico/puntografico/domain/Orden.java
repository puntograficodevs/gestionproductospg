package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orden")
@Getter @Setter
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreCliente;

    @Column(nullable = false)
    private String telefonoCliente;

    @Column(nullable = false)
    private boolean esCuentaCorriente;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaPedido;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaMuestra;

    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaEntrega;

    private String horaEntrega;

    @Column(nullable = false)
    private boolean necesitaFactura;

    private boolean conAdicionalDisenio;

    private boolean facturaHecha;

    @Column(nullable = false)
    private int total;

    private Integer subtotal;

    private Integer precioDisenio;

    @Column(nullable = false)
    private int abonado;

    private String correccion;

    private Integer idEstadoPrevio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_pago")
    private EstadoPago estadoPago;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_estado_orden")
    private EstadoOrden estadoOrden;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();
}
