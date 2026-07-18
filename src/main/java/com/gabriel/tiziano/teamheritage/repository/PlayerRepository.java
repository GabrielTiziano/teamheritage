package com.gabriel.tiziano.teamheritage.repository;

import com.gabriel.tiziano.teamheritage.entities.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    @Override
    @EntityGraph(attributePaths = "club")
    Page<Player> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "club")
    Optional<Player> findById(Long id);

    @EntityGraph(attributePaths = "club")
    Page<Player> findByClubId(Long clubId, Pageable pageable);}
