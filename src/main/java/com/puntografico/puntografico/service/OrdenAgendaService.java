package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Agenda;
import com.puntografico.puntografico.domain.OrdenAgenda;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.repository.OrdenAgendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OrdenAgendaService {

    private final OrdenAgendaRepository ordenAgendaRepository;

    public OrdenAgenda guardar(OrdenTrabajo ordenTrabajo, Agenda agenda, Long idOrdenAgenda) {
        Assert.notNull(ordenTrabajo, "Debe venir una orden de trabajo para enlazar.");
        Assert.notNull(agenda, "Debe venir una agenda para enlazar.");

        OrdenAgenda ordenAgenda = (idOrdenAgenda != null) ? ordenAgendaRepository.findById(idOrdenAgenda).get() : new OrdenAgenda();
        ordenAgenda.setCantidad(agenda.getCantidad());
        ordenAgenda.setOrdenTrabajo(ordenTrabajo);
        ordenAgenda.setAgenda(agenda);

        return ordenAgendaRepository.save(ordenAgenda);
    }

    public OrdenAgenda buscarPorId(Long id) {
        return ordenAgendaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrdenAgenda con ID " + id + " no encontrada"));
    }

    public OrdenAgenda buscarPorOrdenId(Long id) {
        return ordenAgendaRepository.findByOrdenTrabajo_Id(id);
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        ordenAgendaRepository.deleteById(id);
    }
}
