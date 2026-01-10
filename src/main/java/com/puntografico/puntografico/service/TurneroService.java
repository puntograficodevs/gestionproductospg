package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.TurneroDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class TurneroService {

    private final OpcionesTurneroService opcionesTurneroService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final TurneroRepository turneroRepository;

    public Turnero guardar(TurneroDTO turneroDTO, Long idOrdenTrabajo) {
        validarTurneroDTO(turneroDTO);
        Turnero turnero = devolverTurneroCorrespondiente(idOrdenTrabajo);

        MedidaTurnero medidaTurnero = opcionesTurneroService.buscarMedidaTurneroPorId(turneroDTO.getMedidaTurneroId());
        TipoColorTurnero tipoColorTurnero = opcionesTurneroService.buscarTipoColorTurneroPorId(turneroDTO.getTipoColorTurneroId());
        CantidadTurnero cantidadTurnero = opcionesTurneroService.buscarCantidadTurneroPorId(turneroDTO.getCantidadTurneroId());
        Integer cantidad = turneroDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadTurnero.getId() != 5) {
            cantidad = Integer.valueOf(cantidadTurnero.getCantidad());
        }

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

    private Turnero devolverTurneroCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Turnero turneroNuevo = new Turnero();
                    turneroNuevo.setOrdenTrabajo(ordenTrabajo);
                    return turneroNuevo;
                });
    }

    public Optional<Turnero> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return turneroRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Turnero turnero = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        turneroRepository.deleteById(turnero.getId());
    }
}
