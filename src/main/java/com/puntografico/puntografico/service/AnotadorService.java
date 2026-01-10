package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Anotador;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.dto.AnotadorDTO;
import com.puntografico.puntografico.repository.AnotadorRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class AnotadorService {

    private final AnotadorRepository anotadorRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Anotador guardar(AnotadorDTO anotadorDTO, Long idOrdenTrabajo) {
        validarDTO(anotadorDTO);
        Anotador anotador = devolverAnotadorCorrespondiente(idOrdenTrabajo);

        boolean adicionalDisenio = anotadorDTO.getConAdicionalDisenio();

        anotador.setCantidadHojas(anotadorDTO.getCantidadHojas());
        anotador.setMedida(anotadorDTO.getMedida());
        anotador.setTipoTapa(anotadorDTO.getTipoTapa());
        anotador.setConAdicionalDisenio(adicionalDisenio);
        anotador.setEnlaceArchivo(anotadorDTO.getEnlaceArchivo());
        anotador.setInformacionAdicional(anotadorDTO.getInformacionAdicional());
        anotador.setCantidad(anotadorDTO.getCantidad());

        return anotadorRepository.save(anotador);
    }

    private void validarDTO(AnotadorDTO anotadorDTO) {
        Assert.notNull(anotadorDTO.getCantidadHojas(), "La cantidad de hojas es un dato obligatorio.");
        Assert.notNull(anotadorDTO.getTipoTapa(), "El tipo de tapa es un dato obligatorio.");
        Assert.notNull(anotadorDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    private Anotador devolverAnotadorCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Anotador anotadorNuevo = new Anotador();
                    anotadorNuevo.setOrdenTrabajo(ordenTrabajo);
                    return anotadorNuevo;
                });
    }

    public Optional<Anotador> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return anotadorRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Anotador anotador = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Anotador inexistente"));

        anotadorRepository.deleteById(anotador.getId());
    }
}
