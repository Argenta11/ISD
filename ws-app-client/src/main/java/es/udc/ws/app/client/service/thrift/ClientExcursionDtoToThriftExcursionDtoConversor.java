package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.thrift.ThriftExcursionDto;

import java.util.ArrayList;
import java.util.List;

public class ClientExcursionDtoToThriftExcursionDtoConversor {

    public static ThriftExcursionDto toThriftExcursionDto(
            ClientExcursionDto clientExcursionDto) {

        Long excursionId = clientExcursionDto.getExcursionId();

        return new ThriftExcursionDto(
                excursionId == null ? -1 : excursionId.longValue(), clientExcursionDto.getCity(),
                clientExcursionDto.getDescription(), clientExcursionDto.getDate(), clientExcursionDto.getPrice(),
                (short) clientExcursionDto.getMaxParticipants(), (short) clientExcursionDto.getMaxParticipants());

    }

    public static List<ClientExcursionDto> toClientExcursionDto(List<ThriftExcursionDto> excursions) {

        List<ClientExcursionDto> clientExcursionDtos = new ArrayList<>(excursions.size());

        for (ThriftExcursionDto excursion : excursions) {
            clientExcursionDtos.add(toClientExcursionDto(excursion));
        }
        return clientExcursionDtos;

    }

    private static ClientExcursionDto toClientExcursionDto(ThriftExcursionDto excursion) {

        return new ClientExcursionDto(
                excursion.getExcursionId(),
                excursion.getCity(),
                excursion.getDescription(),
                excursion.getDate(),
                (float) excursion.getPrice(),
                excursion.getMaxParticipants(),
                excursion.getNumFree());

    }
}
