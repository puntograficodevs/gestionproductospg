package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.HojasMembreteadas;
import com.puntografico.puntografico.domain.OrdenHojasMembreteadas;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenHojasMembreteadasRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenHojasMembreteadasService {

    private final OrdenHojasMembreteadasRepository ordenHojasMembreteadasRepository;

    public OrdenHojasMembreteadas guardar(OrdenTrabajo ordenTrabajo, HojasMembreteadas hojasMembreteadas, Long idOrdenHojasMembreteadas) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(hojasMembreteadas, "Debe venir una hoja membreteada para enlazar.");

        OrdenHojasMembreteadas ordenHojasMembreteadas = (idOrdenHojasMembreteadas != null) ? ordenHojasMembreteadasRepository.findById(idOrdenHojasMembreteadas).get() : new OrdenHojasMembreteadas();
        ordenHojasMembreteadas.setCantidad(hojasMembreteadas.getCantidad());
        ordenHojasMembreteadas.setOrdenTrabajo(ordenTrabajo);
        ordenHojasMembreteadas.setHojasMembreteadas(hojasMembreteadas);

        return ordenHojasMembreteadasRepository.save(ordenHojasMembreteadas);
    }

    public OrdenHojasMembreteadas buscarPorId(Long id) {
        return ordenHojasMembreteadasRepository.findById(id).orElseThrow(() -> new RuntimeException("OrdenHojasMembreteadas con ID " + id + " no encontrada."));
    }

    public OrdenHojasMembreteadas buscarPorOrdenId(Long id) {
        return ordenHojasMembreteadasRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenHojasMembreteadasRepository.deleteById(id);
    }

}
