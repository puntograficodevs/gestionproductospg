package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Entrada;
import com.puntografico.puntografico.domain.OrdenEntrada;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenEntradaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenEntradaService {

    private final OrdenEntradaRepository ordenEntradaRepository;

    public OrdenEntrada guardar(OrdenTrabajo ordenTrabajo, Entrada entrada, Long idOrdenEntrada) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(entrada, "Debe venir una entrada para enlazar.");

        OrdenEntrada ordenEntrada = (idOrdenEntrada != null) ? ordenEntradaRepository.findById(idOrdenEntrada).get() : new OrdenEntrada();
        ordenEntrada.setCantidad(entrada.getCantidad());
        ordenEntrada.setOrdenTrabajo(ordenTrabajo);
        ordenEntrada.setEntrada(entrada);

        return ordenEntradaRepository.save(ordenEntrada);
    }

    public OrdenEntrada buscarPorId(Long id) {
        return ordenEntradaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenEntrada con ID " + id + " no encontrada"));
    }

    public OrdenEntrada buscarPorOrdenId(Long id) {
        return ordenEntradaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenEntradaRepository.deleteById(id);
    }
}
