package com.puntografico.puntografico.repository;

import com.puntografico.puntografico.domain.Folleto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolletoRepository extends JpaRepository<Folleto, Long> {
}
