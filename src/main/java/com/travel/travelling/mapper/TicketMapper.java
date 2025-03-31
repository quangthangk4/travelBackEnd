package com.travel.travelling.mapper;

import com.travel.travelling.dto.response.TicketBookedResponse;
import com.travel.travelling.dto.response.TicketResponse;
import com.travel.travelling.entity.Ticket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TicketMapper {
    TicketResponse toTicketResponse(Ticket ticket);

    TicketBookedResponse toTicketBookedResponse(Ticket ticket);
}
