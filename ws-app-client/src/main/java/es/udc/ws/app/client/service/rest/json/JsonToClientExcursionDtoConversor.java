package es.udc.ws.app.client.service.rest.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonToClientExcursionDtoConversor {

    public static ObjectNode toObjectNode(ClientExcursionDto excursion) throws IOException {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if (excursion.getExcursionId() != null) {
            excursionObject.put("excursionId", excursion.getExcursionId());
        }
        excursionObject.put("city", excursion.getCity()).
                put("description", excursion.getDescription()).
                put("date", excursion.getDate()).
                put("price", excursion.getPrice()).
                put("maxParticipants", excursion.getMaxParticipants());

        return excursionObject;
    }

    public static ClientExcursionDto toClientExcursionDto(InputStream jsonExcursion) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);
            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                return toClientExcursionDto(rootNode);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    public static List<ClientExcursionDto> toClientExcursionDtos(InputStream jsonExcursions) throws ParsingException {
        try {

            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursions);
            if (rootNode.getNodeType() != JsonNodeType.ARRAY) {
                throw new ParsingException("Unrecognized JSON (array expected)");
            } else {
                ArrayNode excursionsArray = (ArrayNode) rootNode;
                List<ClientExcursionDto> excursionDtos = new ArrayList<>(excursionsArray.size());
                for (JsonNode excursionNode : excursionsArray) {
                    excursionDtos.add(toClientExcursionDto(excursionNode));
                }

                return excursionDtos;
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

    private static ClientExcursionDto toClientExcursionDto(JsonNode excursionNode) throws ParsingException {
        if (excursionNode.getNodeType() != JsonNodeType.OBJECT) {
            throw new ParsingException("Unrecognized JSON (object expected)");
        } else {
            ObjectNode excursionObject = (ObjectNode) excursionNode;

            JsonNode excursionIdNode = excursionObject.get("excursionId");
            Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

            String city = excursionObject.get("city").textValue().trim();
            String description = excursionObject.get("description").textValue().trim();
            String date = excursionObject.get("date").textValue().trim();
            float price = excursionObject.get("price").floatValue();
            int maxParticipants = excursionObject.get("maxParticipants").intValue();
            int numFree = excursionObject.get("numFree").intValue();

            return new ClientExcursionDto(excursionId, city, description, date, price, maxParticipants, numFree);
        }
    }

}
