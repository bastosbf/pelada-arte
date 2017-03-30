package com.bastosbf.pelada.arte.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bastosbf.pelada.arte.server.entity.impl.Pelada;

@Repository
public interface PeladaRepository extends JpaRepository<Pelada, Long> {

}
