package tv.notube.crawler.requester;

import org.apache.log4j.Logger;
import tv.notube.commons.model.Auth;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.activity.Activity;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class DefaultRequester implements Requester {

    private static Logger logger = Logger.getLogger(DefaultRequester.class);

    private static final String PACKAGE = "tv.notube.crawler.requester.request.%sRequest";

    public List<Activity> call(Service service, Auth auth)
            throws RequesterException {
        String serviceClassName = service.getName().substring(0, 1).toUpperCase()
                + service.getName().substring(1, service.getName().length());
        Class<? extends Request> requestClass;
        try {
            requestClass = (Class<? extends Request>)
                    Class.forName(String.format(PACKAGE, serviceClassName));
        } catch (ClassNotFoundException e) {
            final String errMsg = "Error while loading Request class: '" + serviceClassName + "'";
            logger.error(errMsg, e);
            throw new RequesterException(errMsg ,e);
        }
        Request r;
        try {
            r = requestClass.newInstance();
        } catch (InstantiationException e) {
            final String errMsg = "Error while instantiating the request";
            logger.error(errMsg, e);
            throw new RequesterException(errMsg ,e);
        } catch (IllegalAccessException e) {
            final String errMsg = "Error while instantiating the request";
            logger.error(errMsg, e);
            throw new RequesterException(errMsg ,e);
        }
        try {
            r.setAuth(auth);
            r.setService(service);
        } catch (RequestException e) {
            final String errMsg = "Error while configuring the request";
            logger.error(errMsg, e);
            throw new RequesterException(errMsg ,e);
        }
        ServiceResponse response;
        try {
            response = r.call();
        } catch (RequestException e) {
            final String errMsg = "Error while calling request";
            logger.error(errMsg, e);
            throw new RequesterException("" ,e);
        }
        try {
            return (List<Activity>) response.getResponse();
        } catch (ServiceResponseException e) {
            final String errMsg = "Error while getting activities from response";
            logger.error(errMsg, e);
            throw new RequesterException(errMsg ,e);
        }
    }
}
