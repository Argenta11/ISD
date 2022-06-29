package es.udc.ws.app.client.service.thrift;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.thrift.ThriftReservationDto;

import java.util.ArrayList;
import java.util.List;

public class ClientReservationDtoToThriftReservationDtoConversor {

    public static List<ClientReservationDto> toClientReservationDto(List<ThriftReservationDto> reservations) {

        List<ClientReservationDto> clientReservationDtos = new ArrayList<>(reservations.size());

        for (ThriftReservationDto reservation : reservations) {
            clientReservationDtos.add(toClientReservationDto(reservation));
        }
        return clientReservationDtos;

    }

    private static ClientReservationDto toClientReservationDto(ThriftReservationDto reservation) {

        return new ClientReservationDto(
                reservation.getExcursionId(),
                reservation.getReservationId(),
                reservation.getUserEmail(),
                reservation.getCreditCardNumber(),
                reservation.getRegisterDate(),
                (int) reservation.getNumParticipants(),
                reservation.getPrice(),
                reservation.getCanceled());
    }
}
