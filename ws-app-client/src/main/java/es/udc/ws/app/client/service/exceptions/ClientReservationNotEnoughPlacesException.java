package es.udc.ws.app.client.service.exceptions;

public class ClientReservationNotEnoughPlacesException extends Exception{

    private Long reservationId;
    private int places;

    public ClientReservationNotEnoughPlacesException(Long reservationId, int places) {
        super("Reservation for excursion with id=\"" + reservationId +
                "\" has not enough places (places = \"" +
                places + "\")");
        this.reservationId = reservationId;
        this.places = places;
    }

    public Long getReservationId() {
        return reservationId;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }
}
