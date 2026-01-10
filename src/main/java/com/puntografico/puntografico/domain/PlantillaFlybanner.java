package com.puntografico.puntografico.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "plantilla_flybanner")
@Getter
@Setter
public class PlantillaFlybanner {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int precio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_faz_flybanner")
    private TipoFazFlybanner tipoFazFlybanner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_altura_flybanner")
    private AlturaFlybanner alturaFlybanner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bandera_flybanner")
    private BanderaFlybanner banderaFlybanner;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo_base_flybanner")
    private TipoBaseFlybanner tipoBaseFlybanner;
}
