package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientReservationOutOfTimeException extends Exception{

    private Long reservationId;
    private String proposalDate;

    public ClientReservationOutOfTimeException(Long reservationId, String proposalDate) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" can't be completed on (expirationDate = \"" +
                proposalDate + "\")");
        this.reservationId = reservationId;
        this.proposalDate = proposalDate;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(String expirationDate) {
        this.proposalDate = proposalDate;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
