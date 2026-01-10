package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.MedidaViniloPlasticoCorrugado;
import com.puntografico.puntografico.domain.ViniloPlasticoCorrugado;
import com.puntografico.puntografico.dto.ViniloPlasticoCorrugadoDTO;
import com.puntografico.puntografico.repository.ViniloPlasticoCorrugadoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class ViniloPlasticoCorrugadoService {

    private final ViniloPlasticoCorrugadoRepository viniloPlasticoCorrugadoRepository;

    private final OpcionesViniloPlasticoCorrugadoService opcionesViniloPlasticoCorrugadoService;

    public ViniloPlasticoCorrugado guardar(ViniloPlasticoCorrugadoDTO viniloPlasticoCorrugadoDTO, Long idViniloPlasticoCorrugado) {
        validarViniloPlasticoCorrugadoDTO(viniloPlasticoCorrugadoDTO);

        MedidaViniloPlasticoCorrugado medidaViniloPlasticoCorrugado = opcionesViniloPlasticoCorrugadoService.buscarMedidaViniloPlasticoCorrugadoPorId(viniloPlasticoCorrugadoDTO.getMedidaViniloPlasticoCorrugadoId());

        ViniloPlasticoCorrugado viniloPlasticoCorrugado = (idViniloPlasticoCorrugado != null) ? viniloPlasticoCorrugadoRepository.findById(idViniloPlasticoCorrugado).get() : new ViniloPlasticoCorrugado();
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

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        viniloPlasticoCorrugadoRepository.deleteById(id);
    }
}
