package es.udc.ws.app.model.reservation;

import java.sql.Connection;
import java.util.List;

import es.udc.ws.util.exceptions.InstanceNotFoundException;


public interface SqlReservationDao {  
  public Reservation create(Connection connection, Reservation reservation);

  public void update(Connection connection, Reservation reservation) 
     throws InstanceNotFoundException; 
 
  public Reservation find(Connection connection, Long reservationId)  throws InstanceNotFoundException;
  public boolean existsByExcursionId(Connection connection, Long excursionId);
 
  public void remove(Connection connection, Long reservationId) 
     throws InstanceNotFoundException;

  public List<Reservation> findByUserEmail(Connection connection, String userEmail);
}
