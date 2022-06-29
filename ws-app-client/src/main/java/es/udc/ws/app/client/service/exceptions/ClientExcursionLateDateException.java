package es.udc.ws.app.client.service.exceptions;

public class ClientExcursionLateDateException extends Exception{
    private Long excursionId;

    public ClientExcursionLateDateException(Long excursionId) {
        super("Excursion with id=\"" + excursionId + "\" cannot be overtaken");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }
}
