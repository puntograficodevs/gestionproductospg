package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.HojasMembreteadasDTO;
import com.puntografico.puntografico.repository.HojasMembreteadasRepository;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class HojasMembreteadasService {

    private final OpcionesHojasMembreteadasService opcionesHojasMembreteadasService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final HojasMembreteadasRepository hojasMembreteadasRepository;

    public HojasMembreteadas guardar(HojasMembreteadasDTO hojasMembreteadasDTO, Long idOrdenTrabajo) {
        validarHojasMembreteadasDTO(hojasMembreteadasDTO);
        HojasMembreteadas hojasMembreteadas = devolverHojasMembreteadasCorrespondiente(idOrdenTrabajo);

        MedidaHojasMembreteadas medidaHojasMembreteadas = opcionesHojasMembreteadasService.buscarMedidaHojasMembreteadasPorId(hojasMembreteadasDTO.getMedidaHojasMembreteadasId());
        TipoColorHojasMembreteadas tipoColorHojasMembreteadas = opcionesHojasMembreteadasService.buscarTipoColorHojasMembreteadasPorId(hojasMembreteadasDTO.getTipoColorHojasMembreteadasId());
        CantidadHojasMembreteadas cantidadHojasMembreteadas = opcionesHojasMembreteadasService.buscarCantidadHojasMembreteadasPorId(hojasMembreteadasDTO.getCantidadHojasMembreteadasId());
        Integer cantidad = hojasMembreteadasDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadHojasMembreteadas.getId() != 4) {
            cantidad = Integer.valueOf(cantidadHojasMembreteadas.getCantidad());
        }

        boolean adicionalDisenio = hojasMembreteadasDTO.getConAdicionalDisenio();

        hojasMembreteadas.setCantidadHojas(hojasMembreteadasDTO.getCantidadHojas());
        hojasMembreteadas.setEnlaceArchivo(hojasMembreteadasDTO.getEnlaceArchivo());
        hojasMembreteadas.setConAdicionalDisenio(adicionalDisenio);
        hojasMembreteadas.setInformacionAdicional(hojasMembreteadasDTO.getInformacionAdicional());
        hojasMembreteadas.setMedidaHojasMembreteadas(medidaHojasMembreteadas);
        hojasMembreteadas.setTipoColorHojasMembreteadas(tipoColorHojasMembreteadas);
        hojasMembreteadas.setCantidadHojasMembreteadas(cantidadHojasMembreteadas);
        hojasMembreteadas.setCantidad(cantidad);

        if (medidaHojasMembreteadas.getMedida().equalsIgnoreCase("otra")) {
            hojasMembreteadas.setMedidaPersonalizada(hojasMembreteadasDTO.getMedidaPersonalizada());
        } else {
            hojasMembreteadas.setMedidaPersonalizada(null);
        }

        return hojasMembreteadasRepository.save(hojasMembreteadas);
    }

    private void validarHojasMembreteadasDTO(HojasMembreteadasDTO hojasMembreteadasDTO) {
        Assert.notNull(hojasMembreteadasDTO.getMedidaHojasMembreteadasId(), "medidaHojasMembreteadasString es un dato obligatorio.");
        Assert.notNull(hojasMembreteadasDTO.getTipoColorHojasMembreteadasId(), "tipoColorHojasMembreteadasString es un dato obligatorio.");
        Assert.notNull(hojasMembreteadasDTO.getCantidadHojasMembreteadasId(), "cantidadHojasMembreteadasString es un dato obligatorio.");
        Assert.notNull(hojasMembreteadasDTO.getCantidadHojas(), "cantidadHojas es un dato obligatorio.");
    }

    private HojasMembreteadas devolverHojasMembreteadasCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    HojasMembreteadas hojasMembreteadasNueva = new HojasMembreteadas();
                    hojasMembreteadasNueva.setOrdenTrabajo(ordenTrabajo);
                    return hojasMembreteadasNueva;
                });
    }

    public Optional<HojasMembreteadas> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return hojasMembreteadasRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        HojasMembreteadas hojasMembreteadas = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        hojasMembreteadasRepository.deleteById(hojasMembreteadas.getId());
    }
}
