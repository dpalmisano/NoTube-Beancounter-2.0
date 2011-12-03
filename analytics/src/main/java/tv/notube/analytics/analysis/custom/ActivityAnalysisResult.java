package tv.notube.analytics.analysis.custom;

import org.joda.time.DateTime;
import tv.notube.analytics.analysis.AnalysisResult;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class ActivityAnalysisResult extends AnalysisResult {

    private Map<String, Integer> activities = new HashMap<String, Integer>();

    private Map<URL, Integer> services = new HashMap<URL, Integer>();

    public ActivityAnalysisResult(DateTime dateTime) {
        super(dateTime);
    }

    public void add(String verb) {
        if(activities.containsKey(verb)) {
            activities.put(verb, activities.get(verb).intValue() + 1);
        } else {
            activities.put(verb, new Integer(1));
        }
    }

    public int getAmount(String verb) {
        return activities.get(verb);
    }

    public String[] getActivities() {
        return activities.keySet().toArray(
                new String[activities.keySet().size()]
        );
    }

    public int getTotalActivities() {
        int amount = 0;
        for(String activity : activities.keySet()) {
            amount += activities.get(activity).intValue();
        }
        return amount;
    }

       public void add(URL service) {
           if(services.containsKey(service)) {
               services.put(service, services.get(service).intValue() + 1);
           } else {
               services.put(service, new Integer(1));
           }
       }

       public int getAmount(URL service) {
           return services.get(service);
       }

       public URL[] getServices() {
           return services.keySet().toArray(
                   new URL[services.keySet().size()]
           );
       }

       public int getTotalServices() {
           int amount = 0;
           for(URL activity : services.keySet()) {
               amount += services.get(activity).intValue();
           }
           return amount;
       }


}
