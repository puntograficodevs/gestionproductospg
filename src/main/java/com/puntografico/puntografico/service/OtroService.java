package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Otro;
import com.puntografico.puntografico.domain.TipoColorOtro;
import com.puntografico.puntografico.dto.OtroDTO;
import com.puntografico.puntografico.repository.OtroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class OtroService {

    private final OtroRepository otroRepository;

    private final OpcionesOtroService opcionesOtroService;

    public Otro guardar(OtroDTO otroDTO, Long idOtro) {
        validarOtroDTO(otroDTO);

        TipoColorOtro tipoColorOtro = opcionesOtroService.buscarTipoColorOtroPorId(otroDTO.getTipoColorOtroId());

        Otro otro = (idOtro != null) ? otroRepository.findById(idOtro).get() : new Otro();
        boolean adicionalDisenio = otroDTO.getConAdicionalDisenio();

        otro.setMedida(otroDTO.getMedida());
        otro.setEnlaceArchivo(otroDTO.getEnlaceArchivo());
        otro.setConAdicionalDisenio(adicionalDisenio);
        otro.setInformacionAdicional(otroDTO.getInformacionAdicional());
        otro.setTipoColorOtro(tipoColorOtro);
        otro.setCantidad(otroDTO.getCantidad());

        return otroRepository.save(otro);
    }

    private void validarOtroDTO(OtroDTO otroDTO) {
        Assert.notNull(otroDTO.getTipoColorOtroId(), "tipoColorOtroString es un dato obligatorio.");
        Assert.notNull(otroDTO.getCantidad(), "cantidadString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        otroRepository.deleteById(id);
    }
}
