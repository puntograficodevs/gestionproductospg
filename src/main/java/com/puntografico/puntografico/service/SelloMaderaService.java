package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.SelloMadera;
import com.puntografico.puntografico.domain.TamanioSelloMadera;
import com.puntografico.puntografico.dto.SelloMaderaDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.SelloMaderaRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class SelloMaderaService {

    private final SelloMaderaRepository selloMaderaRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesSelloMaderaService opcionesSelloMaderaService;

    public SelloMadera guardar(SelloMaderaDTO selloMaderaDTO, Long idOrdenTrabajo) {
        validarSelloMaderaDTO(selloMaderaDTO);
        SelloMadera selloMadera = devolverSelloMaderaCorrespondiente(idOrdenTrabajo);

        TamanioSelloMadera tamanioSelloMadera = opcionesSelloMaderaService.buscarTamanioSelloMaderaPorId(selloMaderaDTO.getTamanioSelloMaderaId());

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

    private SelloMadera devolverSelloMaderaCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    SelloMadera selloMaderaNuevo = new SelloMadera();
                    selloMaderaNuevo.setOrdenTrabajo(ordenTrabajo);
                    return selloMaderaNuevo;
                });
    }

    public Optional<SelloMadera> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return selloMaderaRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        SelloMadera selloMadera = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        selloMaderaRepository.deleteById(selloMadera.getId());
    }
}
