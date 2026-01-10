package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Agenda;
import com.puntografico.puntografico.domain.TipoColorAgenda;
import com.puntografico.puntografico.domain.TipoTapaAgenda;
import com.puntografico.puntografico.dto.AgendaDTO;
import com.puntografico.puntografico.repository.AgendaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class AgendaService {

    private final AgendaRepository agendaRepository;

    private final OpcionesAgendaService opcionesAgendaService;

    public Agenda guardar(AgendaDTO agendaDTO, Long idAgenda) {
        validarAgendaDTO(agendaDTO);

        TipoColorAgenda tipoColorAgenda = opcionesAgendaService.buscarTipoColorAgendaPorId(agendaDTO.getTipoColorAgendaId());
        TipoTapaAgenda tipoTapaAgenda = opcionesAgendaService.buscarTipoTapaAgendaPorId(agendaDTO.getTipoTapaAgendaId());

        Agenda agenda = (idAgenda != null) ? agendaRepository.findById(idAgenda).get() : new Agenda();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        agendaRepository.deleteById(id);
    }
}
