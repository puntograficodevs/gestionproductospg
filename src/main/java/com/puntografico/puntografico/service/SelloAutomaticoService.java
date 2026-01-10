package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.ModeloSelloAutomatico;
import com.puntografico.puntografico.domain.SelloAutomatico;
import com.puntografico.puntografico.dto.SelloAutomaticoDTO;
import com.puntografico.puntografico.repository.SelloAutomaticoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class SelloAutomaticoService {

    private final SelloAutomaticoRepository selloAutomaticoRepository;

    private final OpcionesSelloAutomaticoService opcionesSelloAutomaticoService;

    public SelloAutomatico guardar(SelloAutomaticoDTO selloAutomaticoDTO, Long idSelloAutomatico) {
        validarSelloAutomaticoDTO(selloAutomaticoDTO);

        ModeloSelloAutomatico modeloSelloAutomatico = opcionesSelloAutomaticoService.buscarModeloSelloAutomaticoPorId(selloAutomaticoDTO.getModeloSelloAutomaticoId());

        SelloAutomatico selloAutomatico = (idSelloAutomatico != null) ? selloAutomaticoRepository.findById(idSelloAutomatico).get() : new SelloAutomatico();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        selloAutomaticoRepository.deleteById(id);
    }
}
