package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Anotador;
import com.puntografico.puntografico.dto.AnotadorDTO;
import com.puntografico.puntografico.repository.AnotadorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service
@Transactional @AllArgsConstructor
public class AnotadorService {

    private final AnotadorRepository anotadorRepository;

    public Anotador guardar(AnotadorDTO anotadorDTO, Long idAnotador) {
        validarDTO(anotadorDTO);

        Anotador anotador = (idAnotador != null) ? anotadorRepository.findById(idAnotador).get() : new Anotador();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        anotadorRepository.deleteById(id);
    }
}
