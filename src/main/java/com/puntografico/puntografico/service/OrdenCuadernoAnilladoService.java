package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CuadernoAnillado;
import com.puntografico.puntografico.domain.OrdenCuadernoAnillado;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenCuadernoAnilladoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenCuadernoAnilladoService {

    private final OrdenCuadernoAnilladoRepository ordenCuadernoAnilladoRepository;

    public OrdenCuadernoAnillado guardar(OrdenTrabajo ordenTrabajo, CuadernoAnillado cuadernoAnillado, Long idOrdenCuadernoAnillado) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(cuadernoAnillado, "Debe venir un cuaderno anillado para enlazar.");

        OrdenCuadernoAnillado ordenCuadernoAnillado = (idOrdenCuadernoAnillado != null) ? ordenCuadernoAnilladoRepository.findById(idOrdenCuadernoAnillado).get() : new OrdenCuadernoAnillado();
        ordenCuadernoAnillado.setCantidad(cuadernoAnillado.getCantidad());
        ordenCuadernoAnillado.setOrdenTrabajo(ordenTrabajo);
        ordenCuadernoAnillado.setCuadernoAnillado(cuadernoAnillado);

        return ordenCuadernoAnilladoRepository.save(ordenCuadernoAnillado);
    }

    public OrdenCuadernoAnillado buscarPorId(Long id) {
        return ordenCuadernoAnilladoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenCuadernoAnillado con ID " + id + "no encontrada"));
    }

    public OrdenCuadernoAnillado buscarPorOrdenId(Long id) {
        return ordenCuadernoAnilladoRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenCuadernoAnilladoRepository.deleteById(id);
    }
}
