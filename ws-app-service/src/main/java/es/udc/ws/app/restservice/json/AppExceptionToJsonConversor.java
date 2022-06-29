package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.model.appservice.exceptions.*;

import java.time.format.DateTimeFormatter;

public class AppExceptionToJsonConversor {

    public static ObjectNode toReservationCanceledException(ReservationCanceledException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservationCanceledException");
        exceptionObject.put("reservationId", (ex.getReservationId() != null) ? ex.getReservationId() : null);
        if (ex.getProposalDate() != null) {
            exceptionObject.put("proposalDate", ex.getProposalDate().toString());
        } else {
            exceptionObject.set("proposalDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toReservationNotSameUserEmailException(ReservationNotSameUserEmailException ex) {
        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservationNotSameUserEmailException");
        exceptionObject.put("reservationId", (ex.getReservationId() != null) ? ex.getReservationId() : null);

        exceptionObject.put("userEmail", ex.getUserEmail());


        return exceptionObject;
    }



    public static ObjectNode toReservationOutOfTimeException(ReservationOutOfTimeException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservationOutOfTimeException");
        exceptionObject.put("reservationId", (ex.getReservationId() != null) ? ex.getReservationId() : null);
        if (ex.getProposalDate() != null) {
            exceptionObject.put("proposalDate", ex.getProposalDate().toString());
        } else {
            exceptionObject.set("proposalDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toReservationNotPossibleException(ReservationNotPossibleException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservationNotPossibleException");
        exceptionObject.put("reservationId", (ex.getReservationId() != null) ? ex.getReservationId() : null);
        if (ex.getProposalDate() != null) {
            exceptionObject.put("proposalDate", ex.getProposalDate().toString());
        } else {
            exceptionObject.set("proposalDate", null);
        }

        return exceptionObject;
    }

    public static ObjectNode toReservationNotEnoughPlacesException(ReservationNotEnoughPlacesException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ReservationNotEnoughPlacesException");
        exceptionObject.put("reservationId", (ex.getReservationId() != null) ? ex.getReservationId() : null);

        exceptionObject.put("places", ex.getPlaces());


        return exceptionObject;
    }
    public static ObjectNode toExcursionNotEnoughPlacesException(ExcursionNotUpdateablePlacesException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ExcursionNotUpdateablePlacesException");
        exceptionObject.put("excursionId", (ex.getExcursionId() != null) ? ex.getExcursionId() : null);

        exceptionObject.put("places", ex.getNumFree());


        return exceptionObject;
    }

    public static ObjectNode toExcursionLateDateException(ExcursionNotUpdateableDatesException ex) {

        ObjectNode exceptionObject = JsonNodeFactory.instance.objectNode();

        exceptionObject.put("errorType", "ExcursionNotUpdateableDatesException");
        exceptionObject.put("excursionId", (ex.getExcursionId() != null) ? ex.getExcursionId() : null);
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date=ex.getDate().format(formatter);

        exceptionObject.put("date",date);


        return exceptionObject;
    }

}
