package es.udc.ws.app.client.service.thrift;

import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.app.client.service.exceptions.*;
import es.udc.ws.app.thrift.*;
import es.udc.ws.util.configuration.ConfigurationParametersManager;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ThriftClientExcursionService implements ClientExcursionService {

    private final static String ENDPOINT_ADDRESS_PARAMETER =
            "ThriftClientExcursionService.endpointAddress";

    private final static String endpointAddress =
            ConfigurationParametersManager.getParameter(ENDPOINT_ADDRESS_PARAMETER);

    private ThriftExcursionService.Client getClient() {

        try {

            TTransport transport = new THttpClient(endpointAddress);
            TProtocol protocol = new TBinaryProtocol(transport);

            return new ThriftExcursionService.Client(protocol);

        } catch (TTransportException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public Long addExcursion(ClientExcursionDto excursion) throws InputValidationException {

        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();

            return client.addExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion)).getExcursionId();

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    @Override
    public void updateExcursion(ClientExcursionDto excursion) throws InputValidationException,
            InstanceNotFoundException, ClientExcursionLateDateException, ClientExcursionNotEnoughPlacesException {

        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();
            client.updateExcursion(ClientExcursionDtoToThriftExcursionDtoConversor.toThriftExcursionDto(excursion));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getMessage());
        } catch (ThriftExcursionNotEnoughPlacesException e) {
            throw new ClientExcursionNotEnoughPlacesException(e.getExcursionId());
        } catch (ThriftExcursionLateDateException e) {
            throw new ClientExcursionLateDateException(e.getExcursionId());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }

    }

    // Se crean para que se pueda compilar el código

    @Override
    public List<ClientExcursionDto> findExcursions(String city, String earlyDate, String lateDates) throws InputValidationException {
        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try  {

            transport.open();

            return ClientExcursionDtoToThriftExcursionDtoConversor.toClientExcursionDto(client.findExcursions(city,earlyDate,lateDates));

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public Long buyReservation(Long excursionId, String userEmail, String creditCardNumber, int numParticipants)
            throws InstanceNotFoundException, InputValidationException,ClientReservationOutOfTimeException,ClientReservationNotEnoughPlacesException {
        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try  {

            transport.open();

            return client.buyReservation(excursionId, userEmail, creditCardNumber,numParticipants);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (ThriftReservationOutOfTimeException e) {
            throw new ClientReservationOutOfTimeException(e.getReservationId(),e.getProposalDate());
        }catch (ThriftReservationNotEnoughPlacesException e) {
            throw new ClientReservationNotEnoughPlacesException(e.getReservationId(),e.getPlaces());
        }  catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            transport.close();
        }
    }

    @Override
    public void cancelReservation(Long reservationId, String userEmail) throws InputValidationException,
            InstanceNotFoundException, ClientReservationNotEnoughPlacesException, ClientReservationOutOfTimeException, ClientReservationCanceledException, ClientReservationNotSameUserEmailException {
        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();

        try  {

            transport.open();
            client.cancelReservation(reservationId, userEmail);

        } catch (ThriftInputValidationException e) {
            throw new InputValidationException(e.getMessage());
        } catch (ThriftInstanceNotFoundException e) {
            throw new InstanceNotFoundException(e.getInstanceId(), e.getInstanceType());
        }catch (ThriftReservationOutOfTimeException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            throw new ClientReservationOutOfTimeException(e.getReservationId(), LocalDateTime.now().format(formatter));
        }catch (ThriftReservationNotEnoughPlacesException e) {
            throw new ClientReservationNotEnoughPlacesException(e.getReservationId(), e.getPlaces());
        }catch (ThriftReservationCanceledException e) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            throw new ClientReservationOutOfTimeException(e.getReservationId(), LocalDateTime.now().format(formatter));
        } catch (ThriftReservationNotSameUserEmailException e) {
            throw new ClientReservationNotSameUserEmailException(e.getReservationId(), userEmail);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            transport.close();
        }

    }
    @Override
    public List<ClientReservationDto> findReservations(String userEmail) {
        ThriftExcursionService.Client client = getClient();
        TTransport transport = client.getInputProtocol().getTransport();
        try {
            transport.open();
            return ClientReservationDtoToThriftReservationDtoConversor.toClientReservationDto(client.findReservations(userEmail));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally{
            transport.close();
        }

    }


}
