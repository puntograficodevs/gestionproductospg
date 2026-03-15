package com.puntografico.puntografico.service;

import com.puntografico.puntografico.domain.Producto;
import com.puntografico.puntografico.repository.ProductoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service @Transactional
@AllArgsConstructor
public class ProductoService {

    private final ProductoRepository productoRepository;

    public List<Producto> buscarTodos() {
        return productoRepository.findAll();
    }

    public Optional<Producto> buscarPorId(Integer id) {
        return productoRepository.findById(id);
    }
}
