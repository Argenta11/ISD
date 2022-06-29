package es.udc.ws.app.model.reservation;

import java.time.LocalDateTime;
import java.util.Objects;

public class Reservation {

    private Long reservationId;
    private Long excursionId;
    private String userEmail;
    private String creditCardNumber;
    private LocalDateTime registerDate;
    private int numParticipants;
    private double price;
    private LocalDateTime canceled;

        // Constructores, getters/setters, equals, hashCode

    public Reservation(Long excursionId, String userEmail, String creditCardNumer, LocalDateTime registerDate,
                       int numParticipants, double price) {
        this.excursionId=excursionId;
        this.userEmail=userEmail;
        this.creditCardNumber=creditCardNumer;
        this.registerDate=registerDate;
        this.numParticipants=numParticipants;
        this.price=price;
    }

    public Reservation(Long reservationId, Long excursionId, String userEmail, String creditCardNumer, LocalDateTime registerDate,
            int numParticipants, double price) {
        this(excursionId, userEmail, creditCardNumer, registerDate, numParticipants, price);
        this.reservationId=reservationId;
    }

    public Reservation(Long reservationId, Long excursionId, String userEmail, String creditCardNumer, LocalDateTime registerDate,
                       int numParticipants, double price, LocalDateTime canceled) {
        this(reservationId, excursionId, userEmail, creditCardNumer, registerDate, numParticipants, price);
        this.canceled=canceled;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public LocalDateTime getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(LocalDateTime registerDate) {
		this.registerDate = (registerDate != null) ? registerDate.withNano(0) : null;
    }

    public String getCreditCardNumber() {
        return creditCardNumber;
    }

    public void setCreditCardNumber(String creditCardNumber) {
        this.creditCardNumber = creditCardNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public void setNumFree(int numParticipants) {
        this.numParticipants = numParticipants;
    }

    public LocalDateTime getCanceled() { return canceled; }

    public void setCanceled(LocalDateTime canceled) { this.canceled=canceled; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return numParticipants == that.numParticipants &&
                Double.compare(that.price, price) == 0 &&
                Objects.equals(reservationId, that.reservationId) &&
                Objects.equals(excursionId, that.excursionId) &&
                Objects.equals(userEmail, that.userEmail) &&
                Objects.equals(creditCardNumber, that.creditCardNumber) &&
                Objects.equals(registerDate, that.registerDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reservationId, excursionId, userEmail, creditCardNumber, registerDate, numParticipants, price);
    }
}
