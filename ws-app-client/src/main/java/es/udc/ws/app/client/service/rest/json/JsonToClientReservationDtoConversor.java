package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientReservationDtoConversor {

    public static ClientReservationDto toClientReservationDto(InputStream jsonReservation) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReservation);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode reservationObject = (ObjectNode) rootNode;

                JsonNode reservationIdNode = reservationObject.get("reservationId");
                Long reservationId = (reservationIdNode != null) ? reservationIdNode.longValue() : null;
                Long excursionId = reservationObject.get("excursionId").longValue();
                String userEmail = reservationObject.get("userEmail").textValue().trim();
                String creditCardNumber = reservationObject.get("creditCardNumber").textValue().trim();
                String registerDate = reservationObject.get("registerDate").textValue().trim();
                int numParticipants = reservationObject.get("numParticipants").intValue();
                double price = reservationObject.get("price").doubleValue();
                String canceled = null;
                if (! reservationObject.get("canceled").isNull()) canceled = reservationObject.get("canceled").textValue().trim();

                return new ClientReservationDto(excursionId, reservationId, userEmail, creditCardNumber, registerDate, numParticipants, price, canceled);

            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientReservationDto> toClientReservationDtos(InputStream jsonReservations) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonReservations);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode reservationsArray = (ArrayNode) rootNode;
                List<ClientReservationDto> reservationDtos = new ArrayList<>(reservationsArray.size());
                for (JsonNode reservationNode : reservationsArray) {
                    reservationDtos.add(toClientReservationDto(reservationNode));
                }

                return reservationDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientReservationDto toClientReservationDto(JsonNode reservationNode) throws ParsingException {
        if (reservationNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode reservationObject = (ObjectNode) reservationNode;

            JsonNode reservationIdNode = reservationObject.get("reservationId");
            Long reservationId = (reservationIdNode != null) ? reservationIdNode.longValue() : null;

            Long excursionId = reservationObject.get("excursionId").longValue();
            String userEmail = reservationObject.get("userEmail").textValue().trim();
            String creditCardNumber = reservationObject.get("creditCardNumber").textValue().trim();
            String registerDate = reservationObject.get("registerDate").textValue().trim();
            int numParticipants = reservationObject.get("numParticipants").intValue();
            float price = reservationObject.get("price").floatValue();
            String canceled = null;
            if (!reservationObject.get("canceled").isNull()) canceled = reservationObject.get("canceled").textValue().trim();

            return new ClientReservationDto(reservationId, excursionId, userEmail, creditCardNumber, registerDate,
                    numParticipants, price, canceled);
        }
    }
}
