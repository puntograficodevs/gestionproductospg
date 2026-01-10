package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TalonarioDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class TalonarioService {

    private final OpcionesTalonarioService opcionesTalonarioService;

    private final TalonarioRepository talonarioRepository;

    public Talonario guardar(TalonarioDTO talonarioDTO, Long idTalonario) {
        validarTalonarioDTO(talonarioDTO);

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

        Talonario talonario = (idTalonario != null) ? talonarioRepository.findById(idTalonario).get() : new Talonario();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        talonarioRepository.deleteById(id);
    }
}
