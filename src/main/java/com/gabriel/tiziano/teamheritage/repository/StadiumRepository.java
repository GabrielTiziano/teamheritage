package com.gabriel.tiziano.teamheritage.repository;

import com.gabriel.tiziano.teamheritage.entities.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StadiumRepository extends JpaRepository<Stadium, Long> {
}
