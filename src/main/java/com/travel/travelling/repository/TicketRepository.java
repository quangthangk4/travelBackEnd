package com.travel.travelling.repository;

import com.travel.travelling.constant.TicketStatus;
import com.travel.travelling.entity.Flight;
import com.travel.travelling.entity.Ticket;
import com.travel.travelling.entity.TicketId;
import com.travel.travelling.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, TicketId> {
    boolean existsByFlightAndSeatNumber(Flight flight, String SeatNumber);

    List<Ticket> findByUser(User user);

    Optional<Ticket> findByFlightIdAndSeatNumberAndStatus(String flightId,String seatNumber, TicketStatus status);

    List<Ticket> findAllByFlightIdAndAvailable(String flightId, boolean available);

    Optional<Ticket> findByUserAndFlight(User user, Flight flight);
}
