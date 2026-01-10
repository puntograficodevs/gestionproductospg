package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.ModeloSelloAutomaticoEscolar;
import com.puntografico.puntografico.domain.SelloAutomaticoEscolar;
import com.puntografico.puntografico.dto.SelloAutomaticoEscolarDTO;
import com.puntografico.puntografico.repository.SelloAutomaticoEscolarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class SelloAutomaticoEscolarService {

    private final SelloAutomaticoEscolarRepository selloAutomaticoEscolarRepository;

    private final OpcionesSelloAutomaticoEscolarService opcionesSelloAutomaticoEscolarService;

    public SelloAutomaticoEscolar guardar(SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO, Long idSelloAutomaticoEscolar) {
        validarSelloAutomaticoEscolarDTO(selloAutomaticoEscolarDTO);

        ModeloSelloAutomaticoEscolar modeloSelloAutomaticoEscolar = opcionesSelloAutomaticoEscolarService.buscarModeloSelloAutomaticoEscolarPorId(selloAutomaticoEscolarDTO.getModeloSelloAutomaticoEscolarId());

        SelloAutomaticoEscolar selloAutomaticoEscolar = (idSelloAutomaticoEscolar != null) ? selloAutomaticoEscolarRepository.findById(idSelloAutomaticoEscolar).get() : new SelloAutomaticoEscolar();
        boolean adicionalDisenio = selloAutomaticoEscolarDTO.getConAdicionalDisenio();

        selloAutomaticoEscolar.setTextoLineaUno(selloAutomaticoEscolarDTO.getTextoLineaUno());
        selloAutomaticoEscolar.setTextoLineaDos(selloAutomaticoEscolarDTO.getTextoLineaDos());
        selloAutomaticoEscolar.setTextoLineaTres(selloAutomaticoEscolarDTO.getTextoLineaTres());
        selloAutomaticoEscolar.setTipografia(selloAutomaticoEscolarDTO.getTipografia());
        selloAutomaticoEscolar.setDibujo(selloAutomaticoEscolarDTO.getDibujo());
        selloAutomaticoEscolar.setEnlaceArchivo(selloAutomaticoEscolarDTO.getEnlaceArchivo());
        selloAutomaticoEscolar.setConAdicionalDisenio(adicionalDisenio);
        selloAutomaticoEscolar.setInformacionAdicional(selloAutomaticoEscolarDTO.getInformacionAdicional());
        selloAutomaticoEscolar.setModeloSelloAutomaticoEscolar(modeloSelloAutomaticoEscolar);
        selloAutomaticoEscolar.setCantidad(selloAutomaticoEscolarDTO.getCantidad());

        return selloAutomaticoEscolarRepository.save(selloAutomaticoEscolar);
    }

    private void validarSelloAutomaticoEscolarDTO(SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO) {
        Assert.notNull(selloAutomaticoEscolarDTO.getTextoLineaUno(), "El texto de la primera línea es obligatorio.");
        Assert.notNull(selloAutomaticoEscolarDTO.getTipografia(), "La tipografía es obligatoria.");
        Assert.notNull(selloAutomaticoEscolarDTO.getDibujo(), "El dibujo es obligatorio.");
        Assert.notNull(selloAutomaticoEscolarDTO.getModeloSelloAutomaticoEscolarId(), "El modelo es un dato obligatorio.");
        Assert.notNull(selloAutomaticoEscolarDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        selloAutomaticoEscolarRepository.deleteById(id);
    }
}
