package es.udc.ws.app.model.appservice;

import es.udc.ws.app.model.appservice.exceptions.*;

import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.util.exceptions.*;


public interface AppService {

    public Excursion createExcursion(Excursion excursion) throws InputValidationException;

    public void updateExcursion(Excursion excursion) throws InputValidationException,
            InstanceNotFoundException, ExcursionNotUpdateablePlacesException, ExcursionNotUpdateableDatesException;

    public List<Excursion> findExcursions(String city, LocalDateTime earlyDate, LocalDateTime lateDate) throws InputValidationException;

    public Reservation buyReservation(Long excursionId, String userEmail, String creditCardNumber, int numParticipants)
            throws InstanceNotFoundException, InputValidationException,
             ReservationOutOfTimeException, ReservationNotEnoughPlacesException;

    public List<Reservation> findReservations(String userEmail);

    public void cancelReservation(Long reservationId, String userEmail) throws InstanceNotFoundException,
            ReservationNotPossibleException, ReservationOutOfTimeException, ReservationCanceledException, ReservationNotSameUserEmailException;

}
