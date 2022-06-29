package es.udc.ws.app.model.appservice.exceptions;

import java.time.LocalDateTime;

public class ExcursionNotUpdateableDatesException extends Exception {
    private Long excursionId;
    private LocalDateTime date;

    public ExcursionNotUpdateableDatesException(Long excursionId,LocalDateTime date) {
        super("Excursion with id=\"" + excursionId + "\n cannot be updated. Cant overtake it: "+date);
        this.excursionId = excursionId;
        this.date=date;
    }

    public Long getExcursionId() {
        return excursionId;
    }

    public void setExcursionId(Long excursionId) {
        this.excursionId = excursionId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
