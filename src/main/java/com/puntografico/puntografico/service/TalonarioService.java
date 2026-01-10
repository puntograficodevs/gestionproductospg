package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TalonarioDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class TalonarioService {

    private final OpcionesTalonarioService opcionesTalonarioService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final TalonarioRepository talonarioRepository;

    public Talonario guardar(TalonarioDTO talonarioDTO, Long idOrdenTrabajo) {
        validarTalonarioDTO(talonarioDTO);
        Talonario talonario = devolverTalonarioCorrespondiente(idOrdenTrabajo);

        TipoTalonario tipoTalonario = opcionesTalonarioService.buscarTipoTalonarioPorId(talonarioDTO.getTipoTalonarioId());
        TipoTroqueladoTalonario tipoTroqueladoTalonario = opcionesTalonarioService.buscarTipoTroqueladoTalonarioPorId(talonarioDTO.getTipoTroqueladoTalonarioId());
        ModoTalonario modoTalonario = opcionesTalonarioService.buscarModoTalonarioPorId(talonarioDTO.getModoTalonarioId());
        TipoColorTalonario tipoColorTalonario = opcionesTalonarioService.buscarTipoColorTalonarioPorId(talonarioDTO.getTipoColorTalonarioId());
        MedidaTalonario medidaTalonario = opcionesTalonarioService.buscarMedidaTalonarioPorId(talonarioDTO.getMedidaTalonarioId());
        TipoPapelTalonario tipoPapelTalonario = opcionesTalonarioService.buscarTipoPapelTalonarioPorId(talonarioDTO.getTipoPapelTalonarioId());
        CantidadTalonario cantidadTalonario = opcionesTalonarioService.buscarCantidadTalonarioPorId(talonarioDTO.getCantidadTalonarioId());

        Integer cantidad = talonarioDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadTalonario.getId() != 5) {
            cantidad = Integer.valueOf(cantidadTalonario.getCantidad());
        }

        boolean adicionalDisenio = talonarioDTO.getConAdicionalDisenio();
        boolean conNumerado = talonarioDTO.getConNumerado();
        boolean esEncolado = talonarioDTO.getEsEncolado();

        talonario.setConNumerado(conNumerado);
        talonario.setCantidadHojas(talonarioDTO.getCantidadHojas());
        talonario.setDetalleNumerado(talonarioDTO.getDetalleNumerado());
        talonario.setEsEncolado(esEncolado);
        talonario.setEnlaceArchivo(talonarioDTO.getEnlaceArchivo());
        talonario.setConAdicionalDisenio(adicionalDisenio);
        talonario.setInformacionAdicional(talonarioDTO.getInformacionAdicional());
        talonario.setTipoTalonario(tipoTalonario);
        talonario.setTipoTroqueladoTalonario(tipoTroqueladoTalonario);
        talonario.setModoTalonario(modoTalonario);
        talonario.setTipoColorTalonario(tipoColorTalonario);
        talonario.setMedidaTalonario(medidaTalonario);
        talonario.setTipoPapelTalonario(tipoPapelTalonario);
        talonario.setCantidadTalonario(cantidadTalonario);
        talonario.setCantidad(cantidad);

        if (medidaTalonario.getMedida().equalsIgnoreCase("otra")) {
            talonario.setMedidaPersonalizada(talonarioDTO.getMedidaPersonalizada());
        } else {
            talonario.setMedidaPersonalizada(null);
        }

        return talonarioRepository.save(talonario);
    }

    private void validarTalonarioDTO(TalonarioDTO talonarioDTO) {
        Assert.notNull(talonarioDTO.getTipoTalonarioId(), "tipoTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getTipoTroqueladoTalonarioId(), "tipoTroqueladoTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getModoTalonarioId(), "modoTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getTipoColorTalonarioId(), "tipoColorTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getMedidaTalonarioId(), "medidaTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getTipoPapelTalonarioId(), "tipoPapelTalonarioString es un dato obligatorio.");
        Assert.notNull(talonarioDTO.getCantidadTalonarioId(), " es un dato obligatorio.");
    }

    private Talonario devolverTalonarioCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Talonario talonarioNuevo = new Talonario();
                    talonarioNuevo.setOrdenTrabajo(ordenTrabajo);
                    return talonarioNuevo;
                });
    }

    public Optional<Talonario> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return talonarioRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Talonario talonario = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        talonarioRepository.deleteById(talonario.getId());
    }
}
