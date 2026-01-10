package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.OrdenViniloPlasticoCorrugado;
import com.puntografico.puntografico.domain.ViniloPlasticoCorrugado;
import com.puntografico.puntografico.repository.OrdenViniloPlasticoCorrugadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenViniloPlasticoCorrugadoService {

    private final OrdenViniloPlasticoCorrugadoRepository ordenViniloPlasticoCorrugadoRepository;

    public OrdenViniloPlasticoCorrugado guardar(OrdenTrabajo ordenTrabajo, ViniloPlasticoCorrugado viniloPlasticoCorrugado, Long idOrdenViniloPlasticoCorrugado) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(viniloPlasticoCorrugado, "Debe venir un vinilo plastico corrugado para enlazar.");

        OrdenViniloPlasticoCorrugado ordenViniloPlasticoCorrugado = (idOrdenViniloPlasticoCorrugado != null) ? ordenViniloPlasticoCorrugadoRepository.findById(idOrdenViniloPlasticoCorrugado).get() : new OrdenViniloPlasticoCorrugado();
        ordenViniloPlasticoCorrugado.setCantidad(viniloPlasticoCorrugado.getCantidad());
        ordenViniloPlasticoCorrugado.setOrdenTrabajo(ordenTrabajo);
        ordenViniloPlasticoCorrugado.setViniloPlasticoCorrugado(viniloPlasticoCorrugado);

        return ordenViniloPlasticoCorrugadoRepository.save(ordenViniloPlasticoCorrugado);
    }

    public OrdenViniloPlasticoCorrugado buscarPorId(Long id) {
        return ordenViniloPlasticoCorrugadoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenViniloPlasticoCorrugado con ID " + id + " no encontrada"));
    }

    public OrdenViniloPlasticoCorrugado buscarPorOrdenId(Long id) {
        return ordenViniloPlasticoCorrugadoRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenViniloPlasticoCorrugadoRepository.deleteById(id);
    }
}
