package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.ModoTalonario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModoTalonarioRepository extends JpaRepository<ModoTalonario, Long> {
}
