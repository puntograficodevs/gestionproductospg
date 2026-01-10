package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadSublimacion;
import com.puntografico.puntografico.domain.MaterialSublimacion;
import com.puntografico.puntografico.domain.Sublimacion;
import com.puntografico.puntografico.dto.SublimacionDTO;
import com.puntografico.puntografico.repository.SublimacionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class SublimacionService {

    private final SublimacionRepository sublimacionRepository;
    private final OpcionesSublimacionService opcionesSublimacionService;

    public Sublimacion guardar(SublimacionDTO sublimacionDTO, Long idSublimacion) {
        validarSublimacionDTO(sublimacionDTO);

        MaterialSublimacion materialSublimacion = opcionesSublimacionService.buscarMaterialSublimacionPorId(sublimacionDTO.getMaterialSublimacionId());
        CantidadSublimacion cantidadSublimacion = opcionesSublimacionService.buscarCantidadSublimacionPorId(sublimacionDTO.getCantidadSublimacionId());

        Integer cantidad = sublimacionDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadSublimacion.getId() != 6) {
            cantidad = Integer.valueOf(cantidadSublimacion.getCantidad());
        }

        Sublimacion sublimacion = (idSublimacion != null) ? sublimacionRepository.findById(idSublimacion).get() : new Sublimacion();
        boolean adicionalDisenio = sublimacionDTO.getConAdicionalDisenio();

        sublimacion.setEnlaceArchivo(sublimacionDTO.getEnlaceArchivo());
        sublimacion.setConAdicionalDisenio(adicionalDisenio);
        sublimacion.setInformacionAdicional(sublimacionDTO.getInformacionAdicional());
        sublimacion.setMaterialSublimacion(materialSublimacion);
        sublimacion.setCantidadSublimacion(cantidadSublimacion);
        sublimacion.setCantidad(cantidad);

        return sublimacionRepository.save(sublimacion);
    }

    private void validarSublimacionDTO(SublimacionDTO sublimacionDTO) {
        Assert.notNull(sublimacionDTO.getMaterialSublimacionId(), "materialSublimacionString es un dato obligatorio.");
        Assert.notNull(sublimacionDTO.getCantidadSublimacionId(), "cantidadSublimacionString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        sublimacionRepository.deleteById(id);
    }
}
