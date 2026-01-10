package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Agenda;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.TipoColorAgenda;
import com.puntografico.puntografico.domain.TipoTapaAgenda;
import com.puntografico.puntografico.dto.AgendaDTO;
import com.puntografico.puntografico.repository.AgendaRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesAgendaService opcionesAgendaService;

    public Agenda guardar(AgendaDTO agendaDTO, Long idOrdenTrabajo) {
        validarAgendaDTO(agendaDTO);
        Agenda agenda = devolverAgendaCorrespondiente(idOrdenTrabajo);

        TipoColorAgenda tipoColorAgenda = opcionesAgendaService.buscarTipoColorAgendaPorId(agendaDTO.getTipoColorAgendaId());
        TipoTapaAgenda tipoTapaAgenda = opcionesAgendaService.buscarTipoTapaAgendaPorId(agendaDTO.getTipoTapaAgendaId());

        boolean adicionalDisenio = agendaDTO.getConAdicionalDisenio();

        agenda.setCantidadHojas(agendaDTO.getCantidadHojas());
        agenda.setMedida(agendaDTO.getMedida());
        agenda.setTipoTapaAgenda(tipoTapaAgenda);
        agenda.setTipoColorAgenda(tipoColorAgenda);
        agenda.setEnlaceArchivo(agendaDTO.getEnlaceArchivo());
        agenda.setInformacionAdicional(agendaDTO.getInformacionAdicional());
        agenda.setCantidad(agendaDTO.getCantidad());
        agenda.setConAdicionalDisenio(adicionalDisenio);

        if (tipoTapaAgenda.getTipo().equalsIgnoreCase("otra")) {
            agenda.setTipoTapaPersonalizada(agendaDTO.getTipoTapaPersonalizada());
        } else {
            agenda.setTipoTapaPersonalizada(null);
        }

        return agendaRepository.save(agenda);
    }

    private void validarAgendaDTO(AgendaDTO agendaDTO) {
        Assert.notNull(agendaDTO.getCantidadHojas(), "La cantidad de hojas es un dato obligatorio.");
        Assert.notNull(agendaDTO.getTipoTapaAgendaId(), "El tipo de tapa es un dato obligatorio.");
        Assert.notNull(agendaDTO.getTipoColorAgendaId(), "El tipo de color es un dato obligatorio.");
        Assert.notNull(agendaDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private Agenda devolverAgendaCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Agenda agendaNueva = new Agenda();
                    agendaNueva.setOrdenTrabajo(ordenTrabajo);
                    return agendaNueva;
                });
    }

    public Optional<Agenda> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return agendaRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Agenda agenda = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Agenda inexistente"));

        agendaRepository.deleteById(agenda.getId());
    }
}
