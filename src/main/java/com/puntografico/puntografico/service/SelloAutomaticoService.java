package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.ModeloSelloAutomatico;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.SelloAutomatico;
import com.puntografico.puntografico.dto.SelloAutomaticoDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.SelloAutomaticoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class SelloAutomaticoService {

    private final SelloAutomaticoRepository selloAutomaticoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesSelloAutomaticoService opcionesSelloAutomaticoService;

    public SelloAutomatico guardar(SelloAutomaticoDTO selloAutomaticoDTO, Long idOrdenTrabajo) {
        validarSelloAutomaticoDTO(selloAutomaticoDTO);
        SelloAutomatico selloAutomatico = devolverSelloAutomaticoCorrespondiente(idOrdenTrabajo);

        ModeloSelloAutomatico modeloSelloAutomatico = opcionesSelloAutomaticoService.buscarModeloSelloAutomaticoPorId(selloAutomaticoDTO.getModeloSelloAutomaticoId());

        boolean esParticular = selloAutomaticoDTO.getEsParticular();
        boolean esProfesional = selloAutomaticoDTO.getEsProfesional();

        selloAutomatico.setEsProfesional(esProfesional);
        selloAutomatico.setEsParticular(esParticular);
        selloAutomatico.setTextoLineaUno(selloAutomaticoDTO.getTextoLineaUno());
        selloAutomatico.setTextoLineaDos(selloAutomaticoDTO.getTextoLineaDos());
        selloAutomatico.setTextoLineaTres(selloAutomaticoDTO.getTextoLineaTres());
        selloAutomatico.setTextoLineaCuatro(selloAutomaticoDTO.getTextoLineaCuatro());
        selloAutomatico.setTipografiaLineaUno(selloAutomaticoDTO.getTipografiaLineaUno());
        selloAutomatico.setEnlaceArchivo(selloAutomaticoDTO.getEnlaceArchivo());
        selloAutomatico.setInformacionAdicional(selloAutomaticoDTO.getInformacionAdicional());
        selloAutomatico.setModeloSelloAutomatico(modeloSelloAutomatico);
        selloAutomatico.setCantidad(selloAutomaticoDTO.getCantidad());

        return selloAutomaticoRepository.save(selloAutomatico);
    }

    private void validarSelloAutomaticoDTO(SelloAutomaticoDTO selloAutomaticoDTO) {
        Assert.notNull(selloAutomaticoDTO.getTextoLineaUno(), "El texto de la primera línea es obligatorio.");
        Assert.notNull(selloAutomaticoDTO.getTipografiaLineaUno(), "La tipografía de la primer línea es obligatoria.");
        Assert.notNull(selloAutomaticoDTO.getModeloSelloAutomaticoId(), "El modelo es un dato obligatorio.");
        Assert.notNull(selloAutomaticoDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private SelloAutomatico devolverSelloAutomaticoCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    SelloAutomatico selloAutomaticoNuevo = new SelloAutomatico();
                    selloAutomaticoNuevo.setOrdenTrabajo(ordenTrabajo);
                    return selloAutomaticoNuevo;
                });
    }

    public Optional<SelloAutomatico> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return selloAutomaticoRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        SelloAutomatico selloAutomatico = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        selloAutomaticoRepository.deleteById(selloAutomatico.getId());
    }
}
