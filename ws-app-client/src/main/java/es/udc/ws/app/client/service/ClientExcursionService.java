package es.udc.ws.app.client.service;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public interface ClientExcursionService {

    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException;

    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException, InstanceNotFoundException, ClientExcursionLateDateException, ClientExcursionNotEnoughPlacesException;

    public List<ClientExcursionDto> findExcursions(String city, String earlyDate, String lateDates) throws InputValidationException;

    public Long buyReservation(Long excursionId, String userEmail, String creditCardNumber, int numParticipants)
            throws InstanceNotFoundException, InputValidationException, ClientReservationNotEnoughPlacesException, ClientReservationOutOfTimeException;

    public void cancelReservation(Long reservationId, String userEmail) throws InputValidationException,
            InstanceNotFoundException, ClientReservationNotEnoughPlacesException, ClientReservationOutOfTimeException, ClientReservationCanceledException, ClientReservationNotSameUserEmailException;

    public List<ClientReservationDto> findReservations(String userEmail);
}