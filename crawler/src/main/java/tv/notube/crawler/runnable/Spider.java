package tv.notube.crawler.runnable;

import org.apache.log4j.Logger;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.User;
import tv.notube.commons.model.UserCredential;
import tv.notube.commons.model.activity.Activity;
import tv.notube.crawler.requester.DefaultRequest;
import tv.notube.crawler.requester.DefaultRequester;
import tv.notube.crawler.requester.Request;
import tv.notube.crawler.requester.RequesterException;
import tv.notube.usermanager.UserManager;
import tv.notube.usermanager.UserManagerException;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManager;
import tv.notube.usermanager.services.auth.ServiceAuthorizationManagerException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Spider implements Runnable {

    private static Logger logger = Logger.getLogger(Spider.class);

    private String name;

    private UserManager um;

    private DefaultRequester requester;

    private User user;

    public Spider(String name, UserManager um, UUID id) throws SpiderException {
        this.name = name;
        this.um = um;
        this.requester = new DefaultRequester();
        try {
            this.user = getUser(id);
        } catch (UserManagerException e) {
            throw new SpiderException("", e);
        }
    }

    public void run() {
        List<Activity> activities;
        ServiceAuthorizationManager sam;
        try {
            sam = um.getServiceAuthorizationManager();
        } catch (UserManagerException e) {
            throw new RuntimeException("", e);
        }
        for (String serviceName : user.getServices()) {
            Service service;
            try {
                service = sam.getService(serviceName);
            } catch (ServiceAuthorizationManagerException e) {
                throw new RuntimeException("", e);
            }
            try {
                activities = requester.call(service, user.getAuth(serviceName));
            } catch (RequesterException e) {
                throw new RuntimeException("", e);
            }
            user.setActivities(activities);
            try {
                um.storeUser(user);
            } catch (UserManagerException e) {
                throw new RuntimeException("", e);
            }
        }
    }

    private User getUser(UUID id) throws UserManagerException {
        return um.getUser(id);
    }

}
