package es.udc.ws.app.thriftservice;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.thrift.ThriftReservationDto;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ReservationToThriftReservationDtoConversor {


    public static ThriftReservationDto toThriftReservationDto(Reservation reservation) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String dateTime = reservation.getRegisterDate().format(formatter);
        String dateCanceled = null;
        if(reservation.getCanceled()!=null){
            dateCanceled = reservation.getCanceled().format(formatter);
        }


        return new ThriftReservationDto( reservation.getExcursionId(),reservation.getReservationId(),
                reservation.getUserEmail(),reservation.getCreditCardNumber(), dateTime,
                (short) reservation.getNumParticipants(), reservation.getPrice(), dateCanceled);
    }

    public static List<ThriftReservationDto> toThriftReservationDtos(List<Reservation> reservations) {
        List<ThriftReservationDto> reservationDtos = new ArrayList<>(reservations.size());
        for (Reservation reservation : reservations) {
            reservationDtos.add(toThriftReservationDto(reservation));
        }
        return reservationDtos;
    }
}


