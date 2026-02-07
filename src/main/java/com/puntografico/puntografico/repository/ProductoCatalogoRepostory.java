package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.ProductoCatalogo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoCatalogoRepostory extends JpaRepository<ProductoCatalogo, Long> {

    List<ProductoCatalogo> findByProductoId(Integer productoId);
}
