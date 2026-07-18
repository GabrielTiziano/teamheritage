package com.gabriel.tiziano.teamheritage.repository;

import com.gabriel.tiziano.teamheritage.entities.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}
