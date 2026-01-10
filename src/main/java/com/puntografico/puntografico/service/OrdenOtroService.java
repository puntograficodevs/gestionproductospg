package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Otro;
import com.puntografico.puntografico.domain.OrdenOtro;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenOtroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenOtroService {

    private final OrdenOtroRepository ordenOtroRepository;

    public OrdenOtro guardar(OrdenTrabajo ordenTrabajo, Otro otro, Long idOrdenOtro) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(otro, "Debe venir algo para enlazar.");

        OrdenOtro ordenOtro = (idOrdenOtro != null) ? ordenOtroRepository.findById(idOrdenOtro).get() : new OrdenOtro();
        ordenOtro.setCantidad(otro.getCantidad());
        ordenOtro.setOrdenTrabajo(ordenTrabajo);
        ordenOtro.setOtro(otro);

        return ordenOtroRepository.save(ordenOtro);
    }

    public OrdenOtro buscarPorId(Long id) {
        return ordenOtroRepository.findById(id).
                orElseThrow(() -> new RuntimeException("OrdenOtro con ID " + id + " no encontrada."));
    }

    public OrdenOtro buscarPorOrdenId(Long id) {
        return ordenOtroRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenOtroRepository.deleteById(id);
    }
}
