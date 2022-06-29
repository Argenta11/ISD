package es.udc.ws.app.client.ui;

import es.udc.ws.app.client.service.ClientExcursionService;
import es.udc.ws.app.client.service.ClientExcursionServiceFactory;
import es.udc.ws.app.client.service.dto.ClientExcursionDto;
import es.udc.ws.app.client.service.dto.ClientReservationDto;
import es.udc.ws.util.exceptions.InputValidationException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

import java.util.List;

public class AppServiceClient {
    public static void main(String[] args) {
        // TODO

        if (args.length == 0) {
            printUsageAndExit();
        }
        ClientExcursionService clientExcursionService =
                ClientExcursionServiceFactory.getService();
        if ("-a".equalsIgnoreCase(args[0])) {
            validateArgs(args, 6, new int[] {5});

            // [add] AppServiceClient -a <city> <description> <date> <price> <maxParticipants>

            try {
                Long excursionId = clientExcursionService.addExcursion(new ClientExcursionDto(null,
                        args[1], args[2], args[3], Float.valueOf(args[4]), Integer.valueOf(args[5])));

                System.out.println("Excursion " + excursionId + " created sucessfully");

            } catch (NumberFormatException | InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-u".equalsIgnoreCase(args[0])) {
            validateArgs(args, 7, new int[] {6});

            // [update] AppServiceClient -u <excursionId> <city> <description> <date> <price> <maxParticipants>

            try {
                clientExcursionService.updateExcursion(new ClientExcursionDto(
                        Long.valueOf(args[1]), args[2], args[3], args[4], Float.valueOf(args[5]),
                        Integer.valueOf(args[6])));

                System.out.println("Excursion " + args[1] + " updated sucessfully");

            } catch (NumberFormatException | InputValidationException |
                    InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        } else if ("-f".equalsIgnoreCase(args[0])) {
            validateArgs(args, 4, new int[] {});

            // [find] AppServiceClient -f <city> <earlyDate> <lateDate>

            try {
                List<ClientExcursionDto> excursions = clientExcursionService.findExcursions(args[1], args[2], args[3]);
                System.out.println("Found " + excursions.size() +
                        " excursion(s) into city '" + args[1] + "'" +
                        " after '"+ args[2] + "' and before '" + args[3] + "'");
                for (int i = 0; i < excursions.size(); i++) {
                    ClientExcursionDto excursionDto = excursions.get(i);
                    System.out.println("Id: " + excursionDto.getExcursionId() +
                            ", City: " + excursionDto.getCity() +
                            ", Description: " + excursionDto.getDescription() +
                            ", Price: " + excursionDto.getPrice() +
                            ", Date: " + excursionDto.getDate() +
                            ", Busy reservations: " + excursionDto.getNumBusy());
                }
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }  else if ("-b".equalsIgnoreCase(args[0])) {
            validateArgs(args, 5, new int[] {1});

            // [buy] AppServiceClient -b <excursionId> <userEmail> <creditCardNumber> <numParticipants>

            Long reservationId;
            try {
                reservationId = clientExcursionService.buyReservation(Long.parseLong(args[1]),
                        args[2], args[3], Integer.valueOf(args[4]));

                System.out.println("Excursion " + args[1] +
                        " reserved sucessfully with reservation number " +
                        reservationId);

            } catch (NumberFormatException | InstanceNotFoundException |
                    InputValidationException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }  else if ("-c".equalsIgnoreCase(args[0])) {
            validateArgs(args, 3, new int[] {1});

            // [cancel] AppServiceClient -c <reservationId> <userEmail>

            try {
                clientExcursionService.cancelReservation(Long.parseLong(args[1]), args[2]);

                System.out.println("Excursion " + args[1] +
                        " of " + args[2] + "has been canceled correctly ");

            } catch (NumberFormatException | InstanceNotFoundException ex) {
                ex.printStackTrace(System.err);
            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }  else if("-l".equalsIgnoreCase(args[0])) {
            validateArgs(args, 2, new int[] {});

            // [cancel] AppServiceClient -l <userEmail>

            try {
                List<ClientReservationDto> reservations = clientExcursionService.findReservations(args[1]);
                System.out.println("Found " + reservations.size() +
                        " reservation(s) from '" + args[1] + "'");
                for (int i = 0; i < reservations.size(); i++) {
                    ClientReservationDto reservationDto = reservations.get(i);
                    System.out.println("reservationId: " + reservationDto.getReservationId() +
                            ", excursionId: : " + reservationDto.getExcursionId() +
                            ", userEmail: " + reservationDto.getUserEmail() +
                            ", creditCardNumber: " + reservationDto.getCreditCardNumber() +
                            ", registerDate: " + reservationDto.getRegisterDate() +
                            ", numParticipants: " + reservationDto.getNumParticipants() +
                            ", price: " + reservationDto.getPrice() +
                            ", canceled: " + reservationDto.getCanceled());
                }

            } catch (Exception ex) {
                ex.printStackTrace(System.err);
            }

        }

    }

    public static void validateArgs(String[] args, int expectedArgs, int[] numericArguments) {
        if(expectedArgs != args.length) {
            printUsageAndExit();
        }
        for(int i = 0 ; i< numericArguments.length ; i++) {
            int position = numericArguments[i];
            try {
                Double.parseDouble(args[position]);
            } catch(NumberFormatException n) {
                printUsageAndExit();
            }
        }
    }

    public static void printUsageAndExit() {
        printUsage();
        System.exit(-1);
    }

    public static void printUsage() {
        System.err.println("Usage:\n" +
                "    [add]    AppServiceClient -a <city> <description> <date> <price> <maxParticipants>\n" +
                "    [update] AppServiceClient -u <excursionId> <city> <description> <date> <price> <maxParticipants>\n" +
                "    [find]   AppServiceClient -f <city> <earlyDate> <lateDate>\n" +
                "    [buy]    AppServiceClient -b <excursionId> <userEmail> <creditCardNumber> <numParticipants>\n" +
                "    [cancel] AppServiceClient -c <reservationId> <userEmail>\n" +
                "    [list]   AppServiceClient -l <userEmail>\n");
    }
}