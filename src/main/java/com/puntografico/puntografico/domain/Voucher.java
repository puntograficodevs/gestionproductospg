package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "voucher")
@Getter
@Setter
public class Voucher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String tipoPapelPersonalizado;

    private String medidaPersonalizada;

    private String enlaceArchivo;

    private boolean conAdicionalDisenio;

    private String informacionAdicional;

    private int cantidad;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_medida_voucher")
    private MedidaVoucher medidaVoucher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_papel_voucher")
    private TipoPapelVoucher tipoPapelVoucher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_voucher")
    private TipoFazVoucher tipoFazVoucher;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_cantidad_voucher")
    private CantidadVoucher cantidadVoucher;
}
