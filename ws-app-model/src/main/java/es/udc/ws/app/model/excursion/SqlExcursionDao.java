package es.udc.ws.app.model.excursion;

import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;

import es.udc.ws.app.model.appservice.exceptions.ExcursionNotModificableException;
import es.udc.ws.util.exceptions.InstanceNotFoundException;

public interface SqlExcursionDao { 
  
  public Excursion create(Connection connection, Excursion excursion); 
 
  public void update(Connection connection, Excursion excursion) 
     throws InstanceNotFoundException;

  public Excursion find(Connection connection, Long excursionId) 
     throws InstanceNotFoundException;

  public List<Excursion> findByDates(Connection connection,
                                     String city, LocalDateTime earlyDate, LocalDateTime lateDate);

  public void remove(Connection connection, Long excursionId)
          throws InstanceNotFoundException, ExcursionNotModificableException;
}
