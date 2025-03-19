package com.travel.travelling.repository;

import com.travel.travelling.entity.Airline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AirlineRepository extends JpaRepository<Airline, String> {
    Optional<Airline> findByName(String name);
}
