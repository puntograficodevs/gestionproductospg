package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.CantidadVoucher;
import com.puntografico.puntografico.domain.MedidaVoucher;
import com.puntografico.puntografico.domain.TipoFazVoucher;
import com.puntografico.puntografico.domain.TipoPapelVoucher;
import com.puntografico.puntografico.repository.CantidadVoucherRepository;
import com.puntografico.puntografico.repository.MedidaVoucherRepository;
import com.puntografico.puntografico.repository.TipoFazVoucherRepository;
import com.puntografico.puntografico.repository.TipoPapelVoucherRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;
import java.util.List;

@Service @Transactional @AllArgsConstructor
public class OpcionesVoucherService {

    private final MedidaVoucherRepository medidaVoucherRepository;
    private final TipoPapelVoucherRepository tipoPapelVoucherRepository;
    private final TipoFazVoucherRepository tipoFazVoucherRepository;
    private final CantidadVoucherRepository cantidadVoucherRepository;

    public List<MedidaVoucher> buscarTodosMedidaVoucher() {
        return medidaVoucherRepository.findAll();
    }

    public List<TipoPapelVoucher> buscarTodosTipoPapelVoucher() {
        return tipoPapelVoucherRepository.findAll();
    }

    public List<TipoFazVoucher> buscarTodosTipoFazVoucher() {
        return tipoFazVoucherRepository.findAll();
    }

    public List<CantidadVoucher> buscarTodosCantidadVoucher() {
        return cantidadVoucherRepository.findAll();
    }

    public MedidaVoucher buscarMedidaVoucherPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return medidaVoucherRepository.findById(id).get();
    }
    public TipoPapelVoucher buscarTipoPapelVoucherPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoPapelVoucherRepository.findById(id).get();
    }
    public TipoFazVoucher buscarTipoFazVoucherPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return tipoFazVoucherRepository.findById(id).get();
    }
    public CantidadVoucher buscarCantidadVoucherPorId(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        return cantidadVoucherRepository.findById(id).get();
    }
}
