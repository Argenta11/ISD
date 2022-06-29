package es.udc.ws.app.model.reservation;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlReservationDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlReservationDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlReservationDao</code>.</li> </ul>
 */
public class SqlReservationDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlReservationDaoFactory.className";
    private static SqlReservationDao dao = null;

    private SqlReservationDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlReservationDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager.getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlReservationDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlReservationDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}
