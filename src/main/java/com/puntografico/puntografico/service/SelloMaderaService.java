package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.SelloMadera;
import com.puntografico.puntografico.domain.TamanioSelloMadera;
import com.puntografico.puntografico.dto.SelloMaderaDTO;
import com.puntografico.puntografico.repository.SelloMaderaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class SelloMaderaService {

    private final SelloMaderaRepository selloMaderaRepository;

    private final OpcionesSelloMaderaService opcionesSelloMaderaService;

    public SelloMadera guardar(SelloMaderaDTO selloMaderaDTO, Long idSelloMadera) {
        validarSelloMaderaDTO(selloMaderaDTO);

        TamanioSelloMadera tamanioSelloMadera = opcionesSelloMaderaService.buscarTamanioSelloMaderaPorId(selloMaderaDTO.getTamanioSelloMaderaId());

        SelloMadera selloMadera = (idSelloMadera != null) ? selloMaderaRepository.findById(idSelloMadera).get() : new SelloMadera();
        boolean adicionalPerilla = selloMaderaDTO.getConAdicionalPerilla();
        boolean adicionalDisenio = selloMaderaDTO.getConAdicionalDisenio();

        selloMadera.setConAdicionalPerilla(adicionalPerilla);
        selloMadera.setDetalleSello(selloMaderaDTO.getDetalleSello());
        selloMadera.setTipografiaLineaUno(selloMaderaDTO.getTipografiaLineaUno());
        selloMadera.setEnlaceArchivo(selloMaderaDTO.getEnlaceArchivo());
        selloMadera.setConAdicionalDisenio(adicionalDisenio);
        selloMadera.setInformacionAdicional(selloMaderaDTO.getInformacionAdicional());
        selloMadera.setTamanioSelloMadera(tamanioSelloMadera);
        selloMadera.setCantidad(selloMaderaDTO.getCantidad());

        if (tamanioSelloMadera.getTamanio().equalsIgnoreCase("otro")) {
            selloMadera.setTamanioPersonalizado(selloMaderaDTO.getTamanioPersonalizado());
        } else {
            selloMadera.setTamanioPersonalizado(null);
        }

        return selloMaderaRepository.save(selloMadera);
    }

    private void validarSelloMaderaDTO(SelloMaderaDTO selloMaderaDTO) {
        Assert.notNull(selloMaderaDTO.getTamanioSelloMaderaId(), "El tamaño es un dato obligatorio.");
        Assert.notNull(selloMaderaDTO.getCantidad(), "La cantidad es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        selloMaderaRepository.deleteById(id);
    }
}
