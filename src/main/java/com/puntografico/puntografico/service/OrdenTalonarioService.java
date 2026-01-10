package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenTalonario;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.Talonario;
import com.puntografico.puntografico.repository.OrdenTalonarioRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenTalonarioService {

    private final OrdenTalonarioRepository ordenTalonarioRepository;

    public OrdenTalonario guardar(OrdenTrabajo ordenTrabajo, Talonario talonario, Long idOrdenTalonario) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(talonario, "Debe venir un talonario para enlazar.");

        OrdenTalonario ordenTalonario = (idOrdenTalonario != null) ? ordenTalonarioRepository.findById(idOrdenTalonario).get() : new OrdenTalonario();
        ordenTalonario.setCantidad(talonario.getCantidad());
        ordenTalonario.setOrdenTrabajo(ordenTrabajo);
        ordenTalonario.setTalonario(talonario);

        return ordenTalonarioRepository.save(ordenTalonario);
    }

    public OrdenTalonario buscarPorId(Long id) {
        return ordenTalonarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenTalonario con ID " + id + " no encontrada"));
    }

    public OrdenTalonario buscarPorOrdenId(Long id) {
        return ordenTalonarioRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenTalonarioRepository.deleteById(id);
    }
}
