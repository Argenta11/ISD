package es.udc.ws.app.client.service.exceptions;

import java.time.LocalDateTime;

public class ClientReservationNotPossibleException extends Exception{


    private Long reservationId;
    private LocalDateTime proposalDate;

    public ClientReservationNotPossibleException(Long reservationId, LocalDateTime proposalDate) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" can't be completed on (expirationDate = \"" +
                proposalDate + "\")");
        this.reservationId = reservationId;
        this.proposalDate = proposalDate;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public LocalDateTime getProposalDate() {
        return proposalDate;
    }

    public void setProposalDate(LocalDateTime expirationDate) {
        this.proposalDate = proposalDate;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
