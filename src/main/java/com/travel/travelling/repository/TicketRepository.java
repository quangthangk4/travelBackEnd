package com.travel.travelling.repository;

import com.travel.travelling.entity.Flight;
import com.travel.travelling.entity.Ticket;
import com.travel.travelling.entity.TicketId;
import com.travel.travelling.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, TicketId> {
    boolean existsByFlightAndSeatNumber(Flight flight, String SeatNumber);

    List<Ticket> findByUser(User user);
}
