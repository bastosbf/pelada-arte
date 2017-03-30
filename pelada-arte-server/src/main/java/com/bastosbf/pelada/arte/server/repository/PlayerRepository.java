package com.bastosbf.pelada.arte.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bastosbf.pelada.arte.server.entity.impl.Player;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {

}
