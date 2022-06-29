package es.udc.ws.app.restservice.dto;

import es.udc.ws.app.model.excursion.Excursion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcursionToRestExcursionDtoConversor {

    public static List<RestExcursionDto> toRestExcursionDtos(List<Excursion> excursions) {
        List<RestExcursionDto> excursionDtos = new ArrayList<>(excursions.size());
        for (int i = 0; i < excursions.size(); i++) {
            Excursion excursion = excursions.get(i);
            excursionDtos.add(toRestExcursionDto(excursion));
        }
        return excursionDtos;
    }

    public static RestExcursionDto toRestExcursionDto(Excursion excursion) {
        return new RestExcursionDto(excursion.getExcursionId(), excursion.getCity(), excursion.getDescription(),
                excursion.getDate().toString(), excursion.getPrice(), excursion.getMaxParticipants(), excursion.getNumFree());
    }

    public static Excursion toExcursion(RestExcursionDto excursion) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime=LocalDateTime.parse(excursion.getDate(), formatter);
        return new Excursion(excursion.getExcursionId(),excursion.getCity(), excursion.getDescription(), excursion.getPrice(), excursion.getMaxParticipants(), excursion.getNumFree(), dateTime);
    }

}