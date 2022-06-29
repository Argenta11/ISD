package es.udc.ws.app.restservice.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import es.udc.ws.app.restservice.dto.RestExcursionDto;
import es.udc.ws.util.json.ObjectMapperFactory;
import es.udc.ws.util.json.exceptions.ParsingException;

import java.io.InputStream;
import java.util.List;

public class JsonToRestExcursionDtoConversor {

    public static ObjectNode toObjectNode(RestExcursionDto excursion) {

        ObjectNode excursionObject = JsonNodeFactory.instance.objectNode();

        if (excursion.getExcursionId() != null) {
            excursionObject.put("excursionId", excursion.getExcursionId());
        }
        excursionObject.put("city", excursion.getCity()).
                put("description", excursion.getDescription()).
                put("price", excursion.getPrice()).
                put("date", excursion.getDate()).
                put("maxParticipants", excursion.getMaxParticipants()).
                put("numFree", excursion.getNumFree());

        return excursionObject;
    }

    public static ArrayNode toArrayNode(List<RestExcursionDto> excursions) {

        ArrayNode excursionNode = JsonNodeFactory.instance.arrayNode();
        for (int i = 0; i < excursions.size(); i++) {
            RestExcursionDto excursionDto = excursions.get(i);
            ObjectNode excursionObject = toObjectNode(excursionDto);
            excursionNode.add(excursionObject);
        }

        return excursionNode;
    }

    public static RestExcursionDto toRestExcursionDto(InputStream jsonExcursion) throws ParsingException {
        try {
            ObjectMapper objectMapper = ObjectMapperFactory.instance();
            JsonNode rootNode = objectMapper.readTree(jsonExcursion);

            if (rootNode.getNodeType() != JsonNodeType.OBJECT) {
                throw new ParsingException("Unrecognized JSON (object expected)");
            } else {
                ObjectNode excursionObject = (ObjectNode) rootNode;

                JsonNode excursionIdNode = excursionObject.get("excursionId");
                Long excursionId = (excursionIdNode != null) ? excursionIdNode.longValue() : null;

                String city = excursionObject.get("city").textValue().trim();
                String description = excursionObject.get("description").textValue().trim();
                float price = excursionObject.get("price").floatValue();
                String date = excursionObject.get("date").textValue().trim();
                int maxParcitipants = excursionObject.get("maxParticipants").intValue();

                return new RestExcursionDto(excursionId,city,description,date,price,maxParcitipants,maxParcitipants);
            }
        } catch (ParsingException ex) {
            throw ex;
        } catch (Exception e) {
            throw new ParsingException(e);
        }
    }

}
