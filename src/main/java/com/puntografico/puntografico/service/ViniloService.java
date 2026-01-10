package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.ViniloDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional @AllArgsConstructor
public class ViniloService {

    private final ViniloRepository viniloRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesViniloService opcionesViniloService;

    public Vinilo guardar(ViniloDTO viniloDTO, Long idOrdenTrabajo) {
        validarViniloDTO(viniloDTO);
        Vinilo vinilo = devolverViniloCorrespondiente(idOrdenTrabajo);

        TipoVinilo tipoVinilo = opcionesViniloService.buscarTipoViniloPorId(viniloDTO.getTipoViniloId());
        TipoAdicionalVinilo tipoAdicionalVinilo = opcionesViniloService.buscarTipoAdicionalViniloPorId(viniloDTO.getTipoAdicionalViniloId());
        TipoCorteVinilo tipoCorteVinilo = opcionesViniloService.buscarTipoCorteViniloPorId(viniloDTO.getTipoCorteViniloId());
        MedidaVinilo medidaVinilo = opcionesViniloService.buscarMedidaViniloPorId(viniloDTO.getMedidaViniloId());
        CantidadVinilo cantidadVinilo = opcionesViniloService.buscarCantidadViniloPorId(viniloDTO.getCantidadViniloId());
        Integer cantidad = viniloDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadVinilo.getId() != 3) {
            cantidad = Integer.valueOf(cantidadVinilo.getCantidad());
        }

        boolean adicionalDisenio = viniloDTO.getConAdicionalDisenio();

        vinilo.setEnlaceArchivo(viniloDTO.getEnlaceArchivo());
        vinilo.setConAdicionalDisenio(adicionalDisenio);
        vinilo.setInformacionAdicional(viniloDTO.getInformacionAdicional());
        vinilo.setTipoVinilo(tipoVinilo);
        vinilo.setTipoAdicionalVinilo(tipoAdicionalVinilo);
        vinilo.setTipoCorteVinilo(tipoCorteVinilo);
        vinilo.setMedidaVinilo(medidaVinilo);
        vinilo.setCantidadVinilo(cantidadVinilo);
        vinilo.setCantidad(cantidad);

        if (medidaVinilo.getMedida().equalsIgnoreCase("otra")) {
            vinilo.setMedidaPersonalizada(viniloDTO.getMedidaPersonalizada());
        } else {
            vinilo.setMedidaPersonalizada(null);
        }

        return viniloRepository.save(vinilo);
    }

    private void validarViniloDTO(ViniloDTO viniloDTO) {
        Assert.notNull(viniloDTO.getTipoViniloId(), "tipoViniloString es un dato obligatorio.");
        Assert.notNull(viniloDTO.getTipoAdicionalViniloId(), "tipoAdicionalViniloString es un dato obligatorio.");
        Assert.notNull(viniloDTO.getTipoCorteViniloId(), "tipoCorteViniloString es un dato obligatorio.");
        Assert.notNull(viniloDTO.getMedidaViniloId(), "medidaViniloString es un dato obligatorio.");
        Assert.notNull(viniloDTO.getCantidadViniloId(), "cantidadViniloString es un dato obligatorio.");
    }

    private Vinilo devolverViniloCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Vinilo viniloNuevo = new Vinilo();
                    viniloNuevo.setOrdenTrabajo(ordenTrabajo);
                    return viniloNuevo;
                });
    }

    public Optional<Vinilo> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return viniloRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Vinilo vinilo = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        viniloRepository.deleteById(vinilo.getId());
    }
}
