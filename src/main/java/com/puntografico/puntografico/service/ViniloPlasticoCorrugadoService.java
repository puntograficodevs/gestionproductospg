package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedidaViniloPlasticoCorrugado;
import com.puntografico.puntografico.domain.OrdenTrabajo;
import com.puntografico.puntografico.domain.ViniloPlasticoCorrugado;
import com.puntografico.puntografico.dto.ViniloPlasticoCorrugadoDTO;
import com.puntografico.puntografico.repository.OrdenTrabajoRepository;
import com.puntografico.puntografico.repository.ViniloPlasticoCorrugadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;
import java.util.Optional;

@Service @Transactional @AllArgsConstructor
public class ViniloPlasticoCorrugadoService {

    private final ViniloPlasticoCorrugadoRepository viniloPlasticoCorrugadoRepository;
    private final OrdenTrabajoRepository ordenTrabajoRepository;
    private final OpcionesViniloPlasticoCorrugadoService opcionesViniloPlasticoCorrugadoService;

    public ViniloPlasticoCorrugado guardar(ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO, Long idOrdenTrabajo) {
        validarViniloPlasticoCorrugadoDTO(viniloPlasticoCorrugadoDTO);
        ViniloPlasticoCorrugado viniloPlasticoCorrugado = devolverViniloPlasticoCorrugadoCorrespondiente(idOrdenTrabajo);

        MedidaViniloPlasticoCorrugado medidaViniloPlasticoCorrugado = opcionesViniloPlasticoCorrugadoService.buscarMedidaViniloPlasticoCorrugadoPorId(viniloPlasticoCorrugadoDTO.getMedidaViniloPlasticoCorrugadoId());

        boolean adicionalDisenio = viniloPlasticoCorrugadoDTO.getConAdicionalDisenio();
        boolean conOjales = viniloPlasticoCorrugadoDTO.getConOjales();

        viniloPlasticoCorrugado.setConOjales(conOjales);
        viniloPlasticoCorrugado.setEnlaceArchivo(viniloPlasticoCorrugadoDTO.getEnlaceArchivo());
        viniloPlasticoCorrugado.setConAdicionalDisenio(adicionalDisenio);
        viniloPlasticoCorrugado.setInformacionAdicional(viniloPlasticoCorrugadoDTO.getInformacionAdicional());
        viniloPlasticoCorrugado.setMedidaViniloPlasticoCorrugado(medidaViniloPlasticoCorrugado);
        viniloPlasticoCorrugado.setCantidad(viniloPlasticoCorrugadoDTO.getCantidad());

        if (medidaViniloPlasticoCorrugado.getMedida().equalsIgnoreCase("otra")) {
            viniloPlasticoCorrugado.setMedidaPersonalizada(viniloPlasticoCorrugadoDTO.getMedidaPersonalizada());
        } else {
            viniloPlasticoCorrugado.setMedidaPersonalizada(null);
        }

        return viniloPlasticoCorrugadoRepository.save(viniloPlasticoCorrugado);
    }

    private void validarViniloPlasticoCorrugadoDTO(ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO) {
        Assert.notNull(viniloPlasticoCorrugadoDTO.getMedidaViniloPlasticoCorrugadoId(), "medidaViniloPlasticoCorrugadoString es un dato obligatorio.");
        Assert.notNull(viniloPlasticoCorrugadoDTO.getCantidad(), "cantidadString es un dato obligatorio.");
    }

    private ViniloPlasticoCorrugado devolverViniloPlasticoCorrugadoCorrespondiente(Long idOrdenTrabajo) {
        OrdenTrabajo ordenTrabajo = ordenTrabajoRepository.findById(idOrdenTrabajo).get();

        return buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseGet(() -> {
                    ViniloPlasticoCorrugado viniloPlasticoCorrugadoNuevo = new ViniloPlasticoCorrugado();
                    viniloPlasticoCorrugadoNuevo.setOrdenTrabajo(ordenTrabajo);
                    return viniloPlasticoCorrugadoNuevo;
                });
    }

    public Optional<ViniloPlasticoCorrugado> buscarPorOrdenTrabajoId(Long idOrdenTrabajo) {
        return viniloPlasticoCorrugadoRepository.findByOrdenTrabajo_Id(idOrdenTrabajo);
    }

    public void eliminar(Long idOrdenTrabajo) {
        ViniloPlasticoCorrugado viniloPlasticoCorrugado = buscarPorOrdenTrabajoId(idOrdenTrabajo)
                .orElseThrow(() -> new RuntimeException("Producto inexistente"));

        viniloPlasticoCorrugadoRepository.deleteById(viniloPlasticoCorrugado.getId());
    }
}
