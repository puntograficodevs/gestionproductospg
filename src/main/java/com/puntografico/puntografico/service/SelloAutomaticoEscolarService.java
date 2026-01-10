package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SelloAutomaticoEscolarDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.SelloAutomaticoEscolarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class SelloAutomaticoEscolarService {

    private final SelloAutomaticoEscolarRepository selloAutomaticoEscolarRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesSelloAutomaticoEscolarService opcionesSelloAutomaticoEscolarService;

    public SelloAutomaticoEscolar guardar(SelloAutomaticoEscolarDTO selloAutomaticoEscolarDTO, Long idOrdenTrabajo) {
        validarSelloAutomaticoEscolarDTO(selloAutomaticoEscolarDTO);
        SelloAutomaticoEscolar selloAutomaticoEscolar = devolverSelloAutomaticoEscolarCorrespondiente(idOrdenTrabajo);

        ModeloSelloAutomaticoEscolar modeloSelloAutomaticoEscolar = opcionesSelloAutomaticoEscolarService.buscarModeloSelloAutomaticoEscolarPorId(selloAutomaticoEscolarDTO.getModeloSelloAutomaticoEscolarId());

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

    private SelloAutomaticoEscolar devolverSelloAutomaticoEscolarCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    SelloAutomaticoEscolar selloAutomaticoEscolarNuevo = new SelloAutomaticoEscolar();
                    selloAutomaticoEscolarNuevo.setOrdenTrabajo(ordenTrabajo);
                    return selloAutomaticoEscolarNuevo;
                });
    }

    public Optional<SelloAutomaticoEscolar> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return selloAutomaticoEscolarRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        SelloAutomaticoEscolar selloAutomaticoEscolar = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        selloAutomaticoEscolarRepository.deleteById(selloAutomaticoEscolar.getId());
    }
}
