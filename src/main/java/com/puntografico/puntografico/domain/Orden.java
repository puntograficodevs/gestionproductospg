package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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

    @Column(nullable = false)
    private String horaEntrega;

    @Column(nullable = false)
    private boolean necesitaFactura;

    @Column(nullable = false)
    private boolean descuentoEfectivo;

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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_encargado_produccion")
    private Empleado encargadoProduccion;

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrdenItem> items = new ArrayList<>();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Pago> pagos = new ArrayList<>();

    @OneToMany(mappedBy = "orden", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Movimiento> movimientos = new ArrayList<>();

    public void agregarMovimiento(Movimiento movimiento) {
        movimientos.add(movimiento);
        movimiento.setOrden(this);
    }

    @Transient
    public String getMediosPagoTexto() {
        if (pagos == null || pagos.isEmpty()) {
            return "Sin pago registrado";
        }

        Set<String> mediosPago = pagos.stream()
                .filter(pago -> pago.getMedioPago() != null)
                .map(pago -> pago.getMedioPago().getMedioDePago())
                .filter(medioPago -> medioPago != null && !medioPago.isBlank())
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return mediosPago.isEmpty()
                ? "Sin pago registrado"
                : String.join(", ", mediosPago);
    }

    @Transient
    public int getMontoDescuentoEfectivo() {
        if (!descuentoEfectivo) {
            return 0;
        }

        return (int) Math.round(total / 9.0);
    }
}
