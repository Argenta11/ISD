package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientReservationCanceledException extends Exception{

    private Long reservationId;
    private String proposalDate;

    public ClientReservationCanceledException(Long reservationId, String proposalDate) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" has already been canceled (expirationDate = \"" +
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

    public void setProposalDate(String proposalDate) {
        this.proposalDate = proposalDate;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
