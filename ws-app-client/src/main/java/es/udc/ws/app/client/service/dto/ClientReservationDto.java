package es.udc.ws.app.client.service.dto;

public class ClientReservationDto {

    private Long reservationId;
    private Long excursionId;
    private String userEmail;
    private String creditCardNumber;
    private String registerDate;
    private int numParticipants;
    private double price;
    private String canceled;


    public ClientReservationDto(Long excursionId,Long reservationId, String userEmail, String creditCardNumber, String registerDate,
                              int numParticipants, double price, String canceled) {
        this.excursionId=excursionId;
        this.reservationId=reservationId;
        this.userEmail=userEmail;
        this.creditCardNumber=creditCardNumber;
        this.registerDate=registerDate;
        this.numParticipants=numParticipants;
        this.price=price;
        this.canceled=(canceled != null) ? canceled : null;
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

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = (registerDate != null) ? registerDate : null;
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

    public String getCanceled() { return canceled; }

    public void setCanceled(String canceled) { this.canceled=canceled; }

}
