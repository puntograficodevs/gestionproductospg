package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.MaterialSublimacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterialSublimacionRepository extends JpaRepository<MaterialSublimacion, Long> {
}
