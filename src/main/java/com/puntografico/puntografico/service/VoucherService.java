package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.*;
import com.puntografico.puntografico.dto.VoucherDTO;
import com.puntografico.puntografico.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.transaction.Transactional;

@Service @Transactional @AllArgsConstructor
public class VoucherService {

    private final VoucherRepository voucherRepository;
    private final OpcionesVoucherService opcionesVoucherService;

    public Voucher guardar(VoucherDTO voucherDTO, Long idVoucher) {
        validarVoucherDTO(voucherDTO);

        MedidaVoucher medidaVoucher = opcionesVoucherService.buscarMedidaVoucherPorId(voucherDTO.getMedidaVoucherId());
        TipoPapelVoucher tipoPapelVoucher = opcionesVoucherService.buscarTipoPapelVoucherPorId(voucherDTO.getTipoPapelVoucherId());
        TipoFazVoucher tipoFazVoucher = opcionesVoucherService.buscarTipoFazVoucherPorId(voucherDTO.getTipoFazVoucherId());
        CantidadVoucher cantidadVoucher = opcionesVoucherService.buscarCantidadVoucherPorId(voucherDTO.getCantidadVoucherId());
        Integer cantidad = voucherDTO.getCantidad();

        if (cantidad == null || cantidad == 0 || cantidadVoucher.getId() != 4) {
            cantidad = Integer.valueOf(cantidadVoucher.getCantidad());
        }

        Voucher voucher = (idVoucher != null) ? voucherRepository.findById(idVoucher).get() : new Voucher();
        boolean adicionalDisenio = voucherDTO.getConAdicionalDisenio();

        voucher.setEnlaceArchivo(voucherDTO.getEnlaceArchivo());
        voucher.setConAdicionalDisenio(adicionalDisenio);
        voucher.setInformacionAdicional(voucherDTO.getInformacionAdicional());
        voucher.setMedidaVoucher(medidaVoucher);
        voucher.setTipoPapelVoucher(tipoPapelVoucher);
        voucher.setTipoFazVoucher(tipoFazVoucher);
        voucher.setCantidadVoucher(cantidadVoucher);
        voucher.setCantidad(cantidad);

        if (medidaVoucher.getMedida().equalsIgnoreCase("otra")) {
            voucher.setMedidaPersonalizada(voucherDTO.getMedidaPersonalizada());
        } else {
            voucher.setMedidaPersonalizada(null);
        }

        if (tipoPapelVoucher.getTipo().equalsIgnoreCase("otro")) {
            voucher.setTipoPapelPersonalizado(voucherDTO.getTipoPapelPersonalizado());
        } else {
            voucher.setTipoPapelPersonalizado(null);
        }

        return voucherRepository.save(voucher);
    }

    private void validarVoucherDTO(VoucherDTO voucherDTO) {
        Assert.notNull(voucherDTO.getMedidaVoucherId(), "medidaVoucherString es un dato obligatorio.");
        Assert.notNull(voucherDTO.getTipoPapelVoucherId(), "tipoPapelVoucherString es un dato obligatorio.");
        Assert.notNull(voucherDTO.getTipoFazVoucherId(), "tipoFazVoucherString es un dato obligatorio.");
        Assert.notNull(voucherDTO.getCantidadVoucherId(), "cantidadVoucherString es un dato obligatorio.");
    }

    public void eliminar(Long id) {
        Assert.notNull(id, "El id no puede ser nulo");
        voucherRepository.deleteById(id);
    }
}
