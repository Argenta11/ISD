package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftExcursionDto;
import es.udc.ws.app.model.excursion.Excursion;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExcursionToThriftExcursionDtoConversor {

    public static Excursion toExcursion(ThriftExcursionDto excursion) {
        DateTimeFormatter formatter=DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTime=LocalDateTime.parse(excursion.getDate(), formatter);
        return new Excursion(excursion.getExcursionId(),excursion.getCity(), excursion.getDescription(),
                (float) excursion.getPrice(), excursion.getMaxParticipants(), excursion.getNumFree(), dateTime);
    }

    public static ThriftExcursionDto toThriftExcursionDto(Excursion excursion) {

        return new ThriftExcursionDto(excursion.getExcursionId(), excursion.getCity(), excursion.getDescription(),
                excursion.getDate().toString(), excursion.getPrice(), (short) excursion.getMaxParticipants(),
                (short) excursion.getNumFree());

    }

    public static List<ThriftExcursionDto> toThriftExcursionDtos(List<Excursion> excursions) {

        List<ThriftExcursionDto> dtos = new ArrayList<>(excursions.size());

        for (Excursion excursion : excursions) {
            dtos.add(toThriftExcursionDto(excursion));
        }
        return dtos;

    }
}
