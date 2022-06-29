package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class ReservationOutOfTimeException extends Exception {

    private Long reservationId;
    private LocalDateTime proposalDate;

    public ReservationOutOfTimeException(Long reservationId, LocalDateTime proposalDate) {
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

    public void setProposalDate(LocalDateTime proposalDate) {
        this.proposalDate = proposalDate;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
