package es.udc.ws.app.model.appservice.exceptions;


public class ReservationNotSameUserEmailException extends Exception{

    private Long reservationId;
    private String userEmail;

    public ReservationNotSameUserEmailException(Long reservationId, String userEmail) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" is not from this email: \"" +
                userEmail + "\")");
        this.reservationId = reservationId;
        this.userEmail = userEmail;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
