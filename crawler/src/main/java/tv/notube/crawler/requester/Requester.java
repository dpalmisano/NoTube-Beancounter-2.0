package tv.notube.crawler.requester;

import tv.notube.commons.model.Auth;
import tv.notube.commons.model.Service;
import tv.notube.commons.model.activity.Activity;

import java.util.List;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public interface Requester {

    public List<Activity> call(Service service, Auth auth)
            throws RequesterException;
}
