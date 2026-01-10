package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.OtroDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.OtroRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class OtroService {

    private final OtroRepository otroRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesOtroService opcionesOtroService;

    public Otro guardar(OtroDTO otroDTO, Long idOrdenTrabajo) {
        validarOtroDTO(otroDTO);
        Otro otro = devolverOtroCorrespondiente(idOrdenTrabajo);

        TipoColorOtro tipoColorOtro = opcionesOtroService.buscarTipoColorOtroPorId(otroDTO.getTipoColorOtroId());

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

    private Otro devolverOtroCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Otro otroNuevo = new Otro();
                    otroNuevo.setOrdenTrabajo(ordenTrabajo);
                    return otroNuevo;
                });
    }

    public Optional<Otro> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return otroRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Otro otro = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        otroRepository.deleteById(otro.getId());
    }
}
