package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.GomaPolimero;
import com.puntografico.puntografico.domain.ModeloGomaPolimero;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.SelloAutomatico;
import com.puntografico.puntografico.dto.GomaPolimeroDTO;
import com.puntografico.puntografico.repository.GomaPolimeroRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class GomaPolimeroService {

    private final GomaPolimeroRepository gomaPolimeroRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesGomaPolimeroService opcionesGomaPolimeroService;

    public GomaPolimero guardar(GomaPolimeroDTO gomaPolimeroDTO, Long idOrdenTrabajo) {
        validarGomaPolimeroDTO(gomaPolimeroDTO);
        GomaPolimero gomaPolimero = devolverGomaPolimeroCorrespondiente(idOrdenTrabajo);

        ModeloGomaPolimero modeloGomaPolimero = opcionesGomaPolimeroService.buscarModeloGomaPolimeroPorId(gomaPolimeroDTO.getModeloGomaPolimeroId());
        boolean adicionalDisenio = gomaPolimeroDTO.getConAdicionalDisenio();

        gomaPolimero.setEnlaceArchivo(gomaPolimeroDTO.getEnlaceArchivo());
        gomaPolimero.setConAdicionalDisenio(adicionalDisenio);
        gomaPolimero.setInformacionAdicional(gomaPolimeroDTO.getInformacionAdicional());
        gomaPolimero.setCantidad(gomaPolimeroDTO.getCantidad());
        gomaPolimero.setModeloGomaPolimero(modeloGomaPolimero);

        return gomaPolimeroRepository.save(gomaPolimero);
    }

    private void validarGomaPolimeroDTO(GomaPolimeroDTO gomaPolimeroDTO) {
        Assert.notNull(gomaPolimeroDTO.getModeloGomaPolimeroId(), "El modelo es un dato obligatorio.");
        Assert.notNull(gomaPolimeroDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private GomaPolimero devolverGomaPolimeroCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
            .orElseGet(() -> {
                GomaPolimero gomaPolimeroNueva = new GomaPolimero();
                gomaPolimeroNueva.setOrdenTrabajo(ordenTrabajo);
                return gomaPolimeroNueva;
            });
    }

    public Optional<GomaPolimero> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return gomaPolimeroRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        GomaPolimero gomaPolimero = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        gomaPolimeroRepository.deleteById(gomaPolimero.getId());
    }
}
