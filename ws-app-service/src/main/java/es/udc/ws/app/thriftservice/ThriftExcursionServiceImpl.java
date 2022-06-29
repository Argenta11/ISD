package es.udc.ws.app.thriftservice;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.excursion.Excursion;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.restservice.dto.ReservationToRestReservationDtoConversor;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.TException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftExcursionServiceImpl implements ThriftExcursionService.Iface {

    @Override
    public ThriftExcursionDto addExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException {

        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try {
            Excursion addedExcursion = AppServiceFactory.getService().createExcursion(excursion);
            return ExcursionToThriftExcursionDtoConversor.toThriftExcursionDto(addedExcursion);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public void updateExcursion(ThriftExcursionDto excursionDto) throws ThriftInputValidationException,
            ThriftInstanceNotFoundException, ThriftExcursionLateDateException,
            ThriftExcursionNotEnoughPlacesException {

        Excursion excursion = ExcursionToThriftExcursionDtoConversor.toExcursion(excursionDto);

        try {
            AppServiceFactory.getService().updateExcursion(excursion);
        } catch (InputValidationException e) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (ExcursionNotUpdateablePlacesException e) {
            throw new ThriftExcursionNotEnoughPlacesException(e.getExcursionId());
        } catch (ExcursionNotUpdateableDatesException e) {
            throw new ThriftExcursionLateDateException(e.getExcursionId());
        }

    }

    @Override
    public List<ThriftExcursionDto> findExcursions(String city, String earlyDate, String lateDate) throws  ThriftInputValidationException {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTimeEarly = LocalDateTime.parse(earlyDate, formatter);
            LocalDateTime dateTimeLate = LocalDateTime.parse(lateDate, formatter);
            List<Excursion> excursions = AppServiceFactory.getService().findExcursions(city, dateTimeEarly, dateTimeLate);

            return ExcursionToThriftExcursionDtoConversor.toThriftExcursionDtos(excursions);
        }catch (InputValidationException e){
            throw new ThriftInputValidationException(e.getMessage());
        }
    }

    @Override
    public long buyReservation(long excursionId, String userEmail, String creditCardNumber, int numParticipants) throws ThriftInstanceNotFoundException,
            ThriftInputValidationException,ThriftReservationOutOfTimeException,ThriftReservationNotEnoughPlacesException {
        try {

            Reservation reservation = AppServiceFactory.getService().buyReservation(excursionId, userEmail, creditCardNumber,numParticipants);
            return reservation.getReservationId();

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch (InputValidationException e ) {
            throw new ThriftInputValidationException(e.getMessage());
        } catch (ReservationOutOfTimeException e){
            throw new ThriftReservationOutOfTimeException();
        } catch (ReservationNotEnoughPlacesException e){
            throw new ThriftReservationNotEnoughPlacesException();
        }
    }

    @Override
    public void cancelReservation(long reservationId, String userEmail) throws ThriftInstanceNotFoundException,
            ThriftReservationNotPossibleException,ThriftReservationOutOfTimeException, ThriftReservationCanceledException,
            ThriftReservationNotSameUserEmailException{
        try {

            AppServiceFactory.getService().cancelReservation(reservationId,userEmail);

        } catch (InstanceNotFoundException e) {
            throw new ThriftInstanceNotFoundException(e.getInstanceId().toString(),
                    e.getInstanceType().substring(e.getInstanceType().lastIndexOf('.') + 1));
        } catch(ReservationNotPossibleException e) {
            throw new ThriftReservationNotPossibleException();
        } catch (ReservationOutOfTimeException e){
            throw new ThriftReservationOutOfTimeException();
        } catch (ReservationCanceledException e){
            throw new ThriftReservationCanceledException();
        } catch (ReservationNotSameUserEmailException e){
            throw new ThriftReservationNotSameUserEmailException();
        }
    }

    @Override
    public List<ThriftReservationDto> findReservations(String userEmail) {
        List<Reservation> reservations = AppServiceFactory.getService().findReservations(userEmail);
        return ReservationToThriftReservationDtoConversor.toThriftReservationDtos(reservations);
    }


}
