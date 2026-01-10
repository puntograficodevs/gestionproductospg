package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.repository.OrdenCatalogoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenCatalogoService {

    private final OrdenCatalogoRepository ordenCatalogoRepository;

    public OrdenCatalogo guardar(OrdenTrabajo ordenTrabajo, Catalogo catalogo, Long idOrdenCatalogo) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(catalogo, "Debe venir un catálogo para enlazar");

        OrdenCatalogo ordenCatalogo = (idOrdenCatalogo != null) ? ordenCatalogoRepository.findById(idOrdenCatalogo).get() : new OrdenCatalogo();
        ordenCatalogo.setCantidad(catalogo.getCantidad());
        ordenCatalogo.setOrdenTrabajo(ordenTrabajo);
        ordenCatalogo.setCatalogo(catalogo);

        return ordenCatalogoRepository.save(ordenCatalogo);
    }

    public OrdenCatalogo buscarPorId(Long id) {
        return ordenCatalogoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenCatalogo con ID " + id + " no encontrada"));
    }

    public OrdenCatalogo buscarPorOrdenId(Long id) {
        return ordenCatalogoRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenCatalogoRepository.deleteById(id);
    }
}
