package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestReservationDto;


import java.util.List;

public class JsonToRestReservationDtoConversor {

    public static ObjectNode toObjectNode(RestReservationDto reservation) {

        ObjectNode reservationObject = JsonNodeFactory.instance.objectNode();

        if (reservation.getReservationId() != null) {
            reservationObject.put("reservationId", reservation.getReservationId());
        }

        if (reservation.getExcursionId() != null) {
            reservationObject.put("excursionId", reservation.getExcursionId());
        }
        reservationObject.put("userEmail", reservation.getUserEmail()).
                put("creditCardNumber", reservation.getCreditCardNumber()).
                put("registerDate", reservation.getRegisterDate()).
                put("numParticipants", reservation.getNumParticipants()).
                put("price", reservation.getPrice()).
                put("canceled", reservation.getCanceled());

        return reservationObject;
    }

    public static ArrayNode toArrayNode(List<RestReservationDto> reservations) {

        ArrayNode reservationNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < reservations.size(); i++) {
            RestReservationDto reservationDto = reservations.get(i);
            ObjectNode reservationObject = toObjectNode(reservationDto);
            reservationNode.add(reservationObject);
        }

        return reservationNode;
    }


}
