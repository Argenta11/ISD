package es.udc.ws.app.model.appservice.exceptions;

public class ExcursionNotModificableException extends Exception {
    private Long excursionId;

    public ExcursionNotModificableException(Long excursionId) {
        super("Excursion with id=\"" + excursionId + "\n cannot be deleted");
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

}
