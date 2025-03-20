package com.travel.travelling.repository;

import com.travel.travelling.entity.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AircraftRepository extends JpaRepository<Aircraft, String> {
    Optional<Aircraft> findByName(String name);
}
