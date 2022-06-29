package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

@SuppressWarnings("serial")
public class ReservationCanceledException extends Exception {

    private Long reservationId;
    private LocalDateTime proposalDate;

    public ReservationCanceledException(Long reservationId, LocalDateTime proposalDate) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" has already been canceled (expirationDate = \"" +
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