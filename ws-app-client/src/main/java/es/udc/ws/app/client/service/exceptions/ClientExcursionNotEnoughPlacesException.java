package es.udc.ws.app.client.service.exceptions;

public class ClientExcursionNotEnoughPlacesException extends Exception {
    private Long excursionId;

    public ClientExcursionNotEnoughPlacesException(Long excursionId) {
        super("Excursion with id=\"" + excursionId + "\" cannot be updated because it has not enough places");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}
