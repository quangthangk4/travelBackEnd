package com.travel.travelling.repository;

import com.travel.travelling.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, String> {
    List<Flight> findAllByFlightNumber(String flightNumber);

    List<Flight> findAllByDepartureAirportAndArrivalAirport(String departureAirport, String arrivalAirport);
}
