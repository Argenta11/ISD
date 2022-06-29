package es.udc.ws.app.model.appservice;

import es.udc.ws.util.configuration.ConfigurationParametersManager;

public class AppServiceFactory {

    private final static String CLASS_NAME_PARAMETER = "AppServiceFactory.className";
    private static AppService service = null;

    private AppServiceFactory() {
    }

    @SuppressWarnings("rawtypes")
    private static AppService getInstance() {
        try {
            String serviceClassName = ConfigurationParametersManager
                    .getParameter(CLASS_NAME_PARAMETER);
            Class serviceClass = Class.forName(serviceClassName);
            return (AppService) serviceClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public synchronized static AppService getService() {

        if (service == null) {
            service = getInstance();
        }
        return service;

    }
}
