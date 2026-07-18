package com.gabriel.tiziano.teamheritage.repository;

import com.gabriel.tiziano.teamheritage.entities.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
    @Override
    @EntityGraph(attributePaths = "stadium")
    Page<Club> findAll(Pageable pageable);

    @Override
    @EntityGraph(attributePaths = "stadium")
    Optional<Club> findById(Long id);
}
