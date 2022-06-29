package es.udc.ws.app.client.service.rest;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.rest.json.JsonToClientExceptionConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientExcursionDtoConversor;
import es.udc.ws.app.client.service.rest.json.JsonToClientReservationDtoConversor;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

public class RestClientExcursionService implements ClientExcursionService {

    private final static String ENDPOINT_ADDRESS_PARAMETER = "RestClientExcursionService.endpointAddress";
    private String endpointAddress;

    private synchronized String getEndpointAddress() {
        if (endpointAddress == null) {
            endpointAddress = ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);
        }
        return endpointAddress;
    }

    private InputStream toInputStream(ClientExcursionDto excursion) {

        try {

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            objectMapper.writer(new DefaultPrettyPrinter()).writeValue(outputStream,
                    JsonToClientExcursionDtoConversor.toObjectNode(excursion));

            return new ByteArrayInputStream(outputStream.toByteArray());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void validateStatusCode(int successCode, HttpResponse response) throws Exception {

        try {

            int statusCode = response.getStatusLine().getStatusCode();

            /* Success? */
            if (statusCode == successCode) {
                return;
            }

            /* Handler error. */
            switch (statusCode) {

                case HttpStatus.SC_NOT_FOUND:
                    throw JsonToClientExceptionConversor.fromNotFoundErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_BAD_REQUEST:
                    throw JsonToClientExceptionConversor.fromBadRequestErrorCode(
                            response.getEntity().getContent());

                case HttpStatus.SC_FORBIDDEN:
                    throw JsonToClientExceptionConversor.fromForbiddenErrorCode(
                            response.getEntity().getContent());

                default:
                    throw new RuntimeException("HTTP error; status code = "
                            + statusCode);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "excursion").
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_CREATED, response);

            return JsonToClientExcursionDtoConversor.toClientExcursionDto(response.getEntity().getContent()).getExcursionId();

        } catch (InputValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException, InstanceNotFoundException,ClientExcursionLateDateException, ClientExcursionNotEnoughPlacesException {

        try {

            HttpResponse response = Request.Put(getEndpointAddress() + "excursion/" + excursion.getExcursionId()).
                    bodyStream(toInputStream(excursion), ContentType.create("application/json")).
                    execute().returnResponse();
            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);
        }catch (ClientExcursionLateDateException | ClientExcursionNotEnoughPlacesException
                | InputValidationException | InstanceNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<ClientExcursionDto> findExcursions(String city, String earlyDate, String lateDate)
                        throws InputValidationException {

        if (city == null || city.equals(" ") || earlyDate == null || earlyDate.equals(" ") || lateDate.equals(" ") || lateDate == null ) {
            throw new InputValidationException("You have to provide city. earlyDate and lateDate.");
        }

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "excursion?city="
                            + URLEncoder.encode(city, "UTF-8") + "&earlyDate="
                            + URLEncoder.encode(earlyDate, "UTF-8") + "&lateDate="
                            + URLEncoder.encode(lateDate, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientExcursionDtoConversor.toClientExcursionDtos(response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long buyReservation(Long excursionId, String userEmail, String creditCardNumber, int numParticipants)
            throws InstanceNotFoundException, InputValidationException, ClientReservationNotEnoughPlacesException, ClientReservationOutOfTimeException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "reservation").
                    bodyForm(
                            Form.form().
                                    add("excursionId", Long.toString(excursionId)).
                                    add("userEmail", userEmail).
                                    add("creditCardNumber", creditCardNumber).
                                    add("numParticipants", Integer.toString(numParticipants)).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_CREATED, response);
            
            return JsonToClientReservationDtoConversor.toClientReservationDto(
                    response.getEntity().getContent()).getReservationId();

        } catch(ClientReservationOutOfTimeException | ClientReservationNotEnoughPlacesException
                | InputValidationException | InstanceNotFoundException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void cancelReservation(Long reservationId, String userEmail) throws InputValidationException, InstanceNotFoundException,
            ClientReservationNotEnoughPlacesException, ClientReservationOutOfTimeException,
            ClientReservationCanceledException, ClientReservationNotSameUserEmailException {

        try {

            HttpResponse response = Request.Post(getEndpointAddress() + "reservation/" + reservationId
                            + "/").
                    bodyForm(
                            Form.form().
                                    add("userEmail", userEmail).
                                    build()).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_NO_CONTENT, response);

        }catch(ClientReservationOutOfTimeException | ClientReservationNotEnoughPlacesException
                | InputValidationException | InstanceNotFoundException | ClientReservationCanceledException
                | ClientReservationNotSameUserEmailException e){
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<ClientReservationDto> findReservations(String userEmail) {

        try {

            HttpResponse response = Request.Get(getEndpointAddress() + "reservation?userEmail="
                            + URLEncoder.encode(userEmail, "UTF-8")).
                    execute().returnResponse();

            validateStatusCode(HttpStatus.SC_OK, response);

            return JsonToClientReservationDtoConversor.toClientReservationDtos(
                    response.getEntity().getContent());

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
