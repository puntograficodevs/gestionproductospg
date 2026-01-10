package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TurneroDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class TurneroService {

    private final OpcionesTurneroService opcionesTurneroService;

    private final TurneroRepository turneroRepository;

    public Turnero guardar(TurneroDTO turneroDTO, Long idTurnero) {
        validarTurneroDTO(turneroDTO);

        MedidaTurnero medidaTurnero = opcionesTurneroService.buscarMedidaTurneroPorId(turneroDTO.getMedidaTurneroId());
        TipoColorTurnero tipoColorTurnero = opcionesTurneroService.buscarTipoColorTurneroPorId(turneroDTO.getTipoColorTurneroId());
        CantidadTurnero cantidadTurnero = opcionesTurneroService.buscarCantidadTurneroPorId(turneroDTO.getCantidadTurneroId());
        Integer cantidad = turneroDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadTurnero.getId() != 5) {
            cantidad = Integer.valueOf(cantidadTurnero.getCantidad());
        }

        Turnero turnero = (idTurnero != null) ? turneroRepository.findById(idTurnero).get() : new Turnero();
        boolean adicionalDisenio = turneroDTO.getConAdicionalDisenio();

        turnero.setCantidadHojas(turneroDTO.getCantidadHojas());
        turnero.setEnlaceArchivo(turneroDTO.getEnlaceArchivo());
        turnero.setConAdicionalDisenio(adicionalDisenio);
        turnero.setInformacionAdicional(turneroDTO.getInformacionAdicional());
        turnero.setMedidaTurnero(medidaTurnero);
        turnero.setTipoColorTurnero(tipoColorTurnero);
        turnero.setCantidadTurnero(cantidadTurnero);
        turnero.setCantidad(cantidad);

        if (medidaTurnero.getMedida().equalsIgnoreCase("otra")) {
            turnero.setMedidaPersonalizada(turneroDTO.getMedidaPersonalizada());
        } else {
            turnero.setMedidaPersonalizada(null);
        }

        return turneroRepository.save(turnero);
    }

    private void validarTurneroDTO(TurneroDTO turneroDTO) {
        Assert.notNull(turneroDTO.getMedidaTurneroId(), "medidaTurneroString es un dato obligatorio.");
        Assert.notNull(turneroDTO.getTipoColorTurneroId(), "tipoColorTurneroString es un dato obligatorio.");
        Assert.notNull(turneroDTO.getCantidadTurneroId(), "cantidadTurneroString es un dato obligatorio.");
        Assert.notNull(turneroDTO.getCantidadHojas(), "cantidadHojas es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        turneroRepository.deleteById(id);
    }
}
