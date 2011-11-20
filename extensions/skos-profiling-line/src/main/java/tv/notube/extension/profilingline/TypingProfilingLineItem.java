package tv.notube.extension.profilingline;

import tv.notube.commons.model.Interest;
import tv.notube.commons.model.Type;
import tv.notube.commons.model.UserProfile;
import tv.notube.extension.profilingline.skos.SkosResolver;
import tv.notube.extension.profilingline.skos.SkosResolverException;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.URI;
import java.util.*;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TypingProfilingLineItem extends ProfilingLineItem {

    private SkosResolver skosResolver;

    public TypingProfilingLineItem(String name, String description) {
        super(name, description);
        skosResolver = new SkosResolver();
    }

    @Override
    public void execute(Object input) throws ProfilingLineItemException {
        WeightedInterests weightedInterests = (WeightedInterests) input;
        Map<URI, Set<Interest>> typedInterests =
                new HashMap<URI, Set<Interest>>();
        for(Interest interest : weightedInterests.getInterests()) {
            URI typeUri;
            try {
                typeUri = skosResolver.getTypes(interest.getReference());
            } catch (SkosResolverException e) {
                throw new ProfilingLineItemException("", e);
            }
            addTypedInterest(typeUri, interest, typedInterests);
        }
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(weightedInterests.getUser());
        userProfile.setVisibility(UserProfile.Visibility.PUBLIC);
        for(URI typeUri : typedInterests.keySet()) {
            Set<Interest> interests = typedInterests.get(typeUri);
            Type type = new Type(interests, module(interests));
            userProfile.addType(type);
        }
        super.getNextProfilingLineItem().execute(userProfile);
    }

    private double module(Set<Interest> interests) {
        Interest vector[] = interests.toArray(
                new Interest[interests.size()]
        );
        double module = 0.0;
        for (int i = 0; i < vector.length - 1; i++) {
            for (int j = i + 1; j < vector.length; j++) {
                module += module(vector[i], vector[j]);
            }
        }
        return Math.sqrt(module);
    }

    private double module(Interest i1, Interest i2) {
        return Math.sqrt(
                Math.pow(i1.getWeight(), 2) + Math.pow(i2.getWeight(), 2)
        );
    }

    private void addTypedInterest(URI typeUri, Interest interest, Map<URI, Set<Interest>> typedInterests) {
        if(typedInterests.keySet().contains(typeUri)) {
            typedInterests.get(typeUri).add(interest);
        } else {
            Set<Interest> interests = new HashSet<Interest>();
            interests.add(interest);
            typedInterests.put(typeUri, interests);
        }
    }
}
