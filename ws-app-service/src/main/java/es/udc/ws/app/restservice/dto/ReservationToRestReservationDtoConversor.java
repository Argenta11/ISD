package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.reservation.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationToRestReservationDtoConversor {

    public static List<RestReservationDto> toRestReservationDtos(List<Reservation> reservations) {
        List<RestReservationDto> reservationDtos = new ArrayList<>(reservations.size());
        for (int i = 0; i < reservations.size(); i++) {
            Reservation reservation = reservations.get(i);
            reservationDtos.add(toRestReservationDto(reservation));
        }
        return reservationDtos;
    }

    public static RestReservationDto toRestReservationDto(Reservation reservation){
        String reservastring=null;
        if(reservation.getCanceled()!=null){
            reservastring=reservation.getCanceled().toString();
        }
        return new RestReservationDto(reservation.getExcursionId(),reservation.getReservationId(), reservation.getUserEmail(), reservation.getCreditCardNumber().substring(reservation.getCreditCardNumber().length()-4),
                reservation.getRegisterDate().toString(), reservation.getNumParticipants(), reservation.getPrice(), reservastring);
    }


}
