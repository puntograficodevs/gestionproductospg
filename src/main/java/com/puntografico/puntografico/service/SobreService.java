package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.SobreDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.SobreRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class SobreService {

    private final SobreRepository sobreRepository;
    private final OpcionesSobreService opcionesSobreService;
    private final OrdenTrabajoRepository ordenTrabajoRepository;

    public Sobre guardar(SobreDTO sobreDTO, Long idOrdenTrabajo) {
        validarSobreDTO(sobreDTO);
        Sobre sobre = devolverSobreCorrespondiente(idOrdenTrabajo);

        MedidaSobre medidaSobre = opcionesSobreService.buscarMedidaSobrePorId(sobreDTO.getMedidaSobreId());
        TipoColorSobre tipoColorSobre = opcionesSobreService.buscarTipoColorSobrePorId(sobreDTO.getTipoColorSobreId());
        CantidadSobre cantidadSobre = opcionesSobreService.buscarCantidadSobrePorId(sobreDTO.getCantidadSobreId());

        Integer cantidad = sobreDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadSobre.getId() != 4) {
            cantidad = Integer.valueOf(cantidadSobre.getCantidad());
        }

        boolean adicionalDisenio = sobreDTO.getConAdicionalDisenio();

        sobre.setEnlaceArchivo(sobreDTO.getEnlaceArchivo());
        sobre.setConAdicionalDisenio(adicionalDisenio);
        sobre.setInformacionAdicional(sobreDTO.getInformacionAdicional());
        sobre.setMedidaSobre(medidaSobre);
        sobre.setTipoColorSobre(tipoColorSobre);
        sobre.setCantidadSobre(cantidadSobre);
        sobre.setCantidad(cantidad);

        if (medidaSobre.getMedida().equalsIgnoreCase("otra")) {
            sobre.setMedidaPersonalizada(sobreDTO.getMedidaPersonalizada());
        } else {
            sobre.setMedidaPersonalizada(null);
        }

        return sobreRepository.save(sobre);
    }

    private void validarSobreDTO(SobreDTO sobreDTO) {
        Assert.notNull(sobreDTO.getMedidaSobreId(), "medidaSobre es un dato obligatorio.");
        Assert.notNull(sobreDTO.getTipoColorSobreId(), "tipoColorSobre es un dato obligatorio.");
        Assert.notNull(sobreDTO.getCantidadSobreId(), "cantidadSobre es un dato obligatorio.");
    }

    private Sobre devolverSobreCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    Sobre sobreNuevo = new Sobre();
                    sobreNuevo.setOrdenTrabajo(ordenTrabajo);
                    return sobreNuevo;
                });
    }

    public Optional<Sobre> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return sobreRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        Sobre sobre = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        sobreRepository.deleteById(sobre.getId());
    }
}
