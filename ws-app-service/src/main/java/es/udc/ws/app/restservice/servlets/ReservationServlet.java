package es.udc.ws.app.restservice.servlets;

import es.udc.ws.app.model.appservice.AppServiceFactory;
import es.udc.ws.app.model.appservice.exceptions.*;
import es.udc.ws.app.model.reservation.Reservation;
import es.udc.ws.app.restservice.dto.ReservationToRestReservationDtoConversor;
import es.udc.ws.app.restservice.dto.RestReservationDto;
import es.udc.ws.app.restservice.json.AppExceptionToJsonConversor;
import es.udc.ws.app.restservice.json.JsonToRestReservationDtoConversor;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.servlet.RestHttpServletTemplate;
import es.udc.ws.util.servlet.ServletUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReservationServlet extends RestHttpServletTemplate {

    protected void processPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, InstanceNotFoundException, InputValidationException {

        if(req.getPathInfo()==null){
            Long excursionId=ServletUtils.getMandatoryParameterAsLong(req,"excursionId");
            String userEmail=ServletUtils.getMandatoryParameter(req,"userEmail");
            String creditCardNumber=ServletUtils.getMandatoryParameter(req,"creditCardNumber");
            int numParticipants= Integer.parseInt(ServletUtils.getMandatoryParameter(req,"numParticipants"));
            Reservation reservation=null;

            try {
                reservation= AppServiceFactory.getService().buyReservation(excursionId,userEmail,creditCardNumber,numParticipants);

                assert reservation != null;
                RestReservationDto reservationDto= ReservationToRestReservationDtoConversor.toRestReservationDto(reservation);
                String reservationURL=ServletUtils.normalizePath(req.getRequestURL().toString())+"/"+ reservation.getReservationId().toString();

                Map<String,String> headers=new HashMap<>(1);
                headers.put("Location",reservationURL);
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_CREATED, JsonToRestReservationDtoConversor.toObjectNode(reservationDto),headers);

            } catch (ReservationNotEnoughPlacesException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NOT_FOUND, AppExceptionToJsonConversor.toReservationNotEnoughPlacesException(e),null);
            } catch (ReservationOutOfTimeException e){
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toReservationOutOfTimeException(e),null);
            }
        }
        else{
            Long reservationId=ServletUtils.getIdFromPath(req,"reservation");
            String userEmail=ServletUtils.getMandatoryParameter(req,"userEmail");

            try {
                AppServiceFactory.getService().cancelReservation(reservationId,userEmail);

                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_NO_CONTENT, null, null);

            } catch (ReservationNotPossibleException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toReservationNotPossibleException(e), null);
            } catch (ReservationOutOfTimeException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toReservationOutOfTimeException(e), null);
            } catch (ReservationCanceledException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toReservationCanceledException(e), null);
            } catch (ReservationNotSameUserEmailException e) {
                ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_FORBIDDEN, AppExceptionToJsonConversor.toReservationNotSameUserEmailException(e), null);
            }
        }


    }

    protected void processGet(HttpServletRequest req, HttpServletResponse resp) throws IOException,
            InputValidationException {
        String userEmail = ServletUtils.getMandatoryParameter(req, "userEmail");

        List<Reservation> reservations;

        reservations = AppServiceFactory.getService().findReservations(userEmail);

        List<RestReservationDto> reservationDtos = ReservationToRestReservationDtoConversor.toRestReservationDtos(reservations);
        ServletUtils.writeServiceResponse(resp, HttpServletResponse.SC_OK,
                JsonToRestReservationDtoConversor.toArrayNode(reservationDtos), null);
    }
}
