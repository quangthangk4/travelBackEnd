package com.travel.travelling.repository;

import com.travel.travelling.entity.Flight;
import com.travel.travelling.entity.Ticket;
import com.travel.travelling.entity.TicketId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, TicketId> {
    boolean existsByFlightAndSeatNumber(Flight flight, String SeatNumber);
}
