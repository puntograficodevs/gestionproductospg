package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenSelloAutomaticoEscolar;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.SelloAutomaticoEscolar;
import com.puntografico.puntografico.repository.OrdenSelloAutomaticoEscolarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class OrdenSelloAutomaticoEscolarService {

    private final OrdenSelloAutomaticoEscolarRepository ordenSelloAutomaticoEscolarRepository;

    public OrdenSelloAutomaticoEscolar guardar(OrdenTrabajo ordenTrabajo, SelloAutomaticoEscolar selloAutomaticoEscolar, Long idOrdenSelloAutomaticoEscolar) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(selloAutomaticoEscolar, "Debe venir un sello para enlazar.");

        OrdenSelloAutomaticoEscolar ordenSelloAutomaticoEscolar = (idOrdenSelloAutomaticoEscolar != null) ? ordenSelloAutomaticoEscolarRepository.findById(idOrdenSelloAutomaticoEscolar).get() : new OrdenSelloAutomaticoEscolar();
        ordenSelloAutomaticoEscolar.setSelloAutomaticoEscolar(selloAutomaticoEscolar);
        ordenSelloAutomaticoEscolar.setOrdenTrabajo(ordenTrabajo);
        ordenSelloAutomaticoEscolar.setCantidad(selloAutomaticoEscolar.getCantidad());

        return ordenSelloAutomaticoEscolarRepository.save(ordenSelloAutomaticoEscolar);
    }

    public OrdenSelloAutomaticoEscolar buscarPorId(Long id) {
        return ordenSelloAutomaticoEscolarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenSelloAutomaticoEscolar con ID " + id + " no encontrada."));
    }

    public OrdenSelloAutomaticoEscolar buscarPorOrdenId(Long id) {
        return ordenSelloAutomaticoEscolarRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenSelloAutomaticoEscolarRepository.deleteById(id);
    }
}
