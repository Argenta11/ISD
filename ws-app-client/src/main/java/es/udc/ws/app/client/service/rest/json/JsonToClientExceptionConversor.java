package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;

public class JsonToClientExceptionConversor {

    public static Exception fromBadRequestErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InputValidation")) {
                    return toInputValidationException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InputValidationException toInputValidationException(JsonNode rootNode) {
        String message = rootNode.get("message").textValue();
        return new InputValidationException(message);
    }

    public static Exception fromNotFoundErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("InstanceNotFound")) {
                    return toInstanceNotFoundException(rootNode);
                } else if(errorType.equals("ReservationNotEnoughPlacesException")){
                    return toReservationNotEnoughPlacesException(rootNode);
                } else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static InstanceNotFoundException toInstanceNotFoundException(JsonNode rootNode) {
        String instanceId = rootNode.get("instanceId").textValue();
        String instanceType = rootNode.get("instanceType").textValue();
        return new InstanceNotFoundException(instanceId, instanceType);
    }
    public static Exception fromForbiddenErrorCode(InputStream ex) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(ex);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                String errorType = rootNode.get("errorType").textValue();
                if (errorType.equals("ExcursionNotUpdateableDatesException")) {
                    return toExcursionNotUpdateableDatesException(rootNode);
                } else if(errorType.equals("ExcursionNotUpdateablePlacesException")){
                    return toExcursionNotUpdateablePlacesException(rootNode);
                }else if( errorType.equals("ReservationOutOfTimeException")){
                    return toReservationOutOfTimeException(rootNode);
                }else if(errorType.equals("ReservationCanceledException")){
                    return toReservationCanceledException(rootNode);
                } else if(errorType.equals("ReservationNotSameUserEmailException")){
                    return toReservationNotSameUserEmailException(rootNode);
                }else {
                    throw new ParsingException("Unrecognized error type: " + errorType);
                }
            }
        } catch (ParsingException e) {
            throw e;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }
    private static ClientExcursionLateDateException toExcursionNotUpdateableDatesException(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientExcursionLateDateException(excursionId);
    }

    private static ClientExcursionNotEnoughPlacesException toExcursionNotUpdateablePlacesException(JsonNode rootNode) {
        Long excursionId = rootNode.get("excursionId").longValue();
        return new ClientExcursionNotEnoughPlacesException(excursionId);
    }

    private static ClientReservationNotEnoughPlacesException toReservationNotEnoughPlacesException(JsonNode rootNode) {
        Long reservationId = rootNode.get("reservationId").longValue();
        int places = rootNode.get("places").asInt();
        return new ClientReservationNotEnoughPlacesException(reservationId,places);
    }

    private static ClientReservationOutOfTimeException toReservationOutOfTimeException(JsonNode rootNode) {
        Long reservationId = rootNode.get("reservationId").longValue();
        String proposalDate = rootNode.get("proposalDate").toString();
        return new ClientReservationOutOfTimeException(reservationId,proposalDate);
    }

    private static ClientReservationCanceledException toReservationCanceledException(JsonNode rootNode) {
        Long reservationId = rootNode.get("reservationId").longValue();
        String proposalDate = rootNode.get("proposalDate").toString();
        return new ClientReservationCanceledException(reservationId,proposalDate);
    }
    private static ClientReservationNotSameUserEmailException toReservationNotSameUserEmailException(JsonNode rootNode) {
        Long reservationId = rootNode.get("reservationId").longValue();
        String userEmail = rootNode.get("userEmail").toString();
        return new ClientReservationNotSameUserEmailException(reservationId,userEmail);
    }
}
