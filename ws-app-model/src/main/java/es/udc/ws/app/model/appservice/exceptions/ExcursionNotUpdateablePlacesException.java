package es.udc.ws.app.model.appservice.exceptions;

public class ExcursionNotUpdateablePlacesException extends Exception {
    private Long excursionId;
    private int numFree;

    public ExcursionNotUpdateablePlacesException(Long excursionId,int text) {
        super("Excursion with id=\"" + excursionId + "\n cannot be updated not enough places: "+text);
        this.excursionId = excursionId;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public int getNumFree() {
        return numFree;
    }

    public void setNumFree(int numFree) {
        this.numFree = numFree;
    }

}
