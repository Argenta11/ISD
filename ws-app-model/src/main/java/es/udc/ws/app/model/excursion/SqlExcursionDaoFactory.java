package es.udc.ws.app.model.excursion;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

/**
 * A factory to get
 * <code>SqlExcursionDao</code> objects. <p> Required configuration parameters: <ul>
 * <li><code>SqlExcursionDaoFactory.className</code>: it must specify the full class
 * name of the class implementing
 * <code>SqlExcursionDao</code>.</li> </ul>
 */
public class SqlExcursionDaoFactory {

    private final static String CLASS_NAME_PARAMETER = "SqlExcursionDaoFactory.className";
    private static SqlExcursionDao dao = null;

    private SqlExcursionDaoFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static SqlExcursionDao getInstance() {
        try {
            String daoClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class daoClass = Class.forName(daoClassName);
            return (SqlExcursionDao) daoClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static SqlExcursionDao getDao() {

        if (dao == null) {
            dao = getInstance();
        }
        return dao;

    }
}