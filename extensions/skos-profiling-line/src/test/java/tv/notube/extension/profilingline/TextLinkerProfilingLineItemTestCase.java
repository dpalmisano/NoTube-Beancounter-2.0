package tv.notube.extension.profilingline;

import org.joda.time.DateTime;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import tv.notube.commons.model.User;
import tv.notube.commons.model.activity.Activity;
import tv.notube.commons.model.activity.Context;
import tv.notube.commons.model.activity.Song;
import tv.notube.commons.model.activity.Tweet;
import tv.notube.commons.model.activity.Verb;
import tv.notube.profiler.line.ProfilingLineItem;
import tv.notube.profiler.line.ProfilingLineItemException;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class TextLinkerProfilingLineItemTestCase {

    private ProfilingLineItem initItem;

    private ProfilingLineItem textItem;

    private ProfilingLineItem mbItem;

    private ProfilingLineItem skos;

    private ProfilingLineItem weight;

    @BeforeTest
    public void setUp() {
        initItem = new InitProfilingLineItem("init", "prepares the objects");
        textItem = new TextLinkerProfilingLineItem(
                "text-linker",
                "it provides dbpedia URLs"
        );
        mbItem = new MusicBrainzLinkerProfilingLineItem(
                "music-brainz", "links to dbpedia resolving mbrainz ids"
        );
        skos = new SkosProfilingLineItem("skos", "resolving SKOS subjects");
        weight = new WeightingProfilingLineItem("weight", "weighting interests");

        weight.setNextProfilingLineItem(new TestDumpProfilingLineItem("test", "this just dumps"));
        skos.setNextProfilingLineItem(weight);
        mbItem.setNextProfilingLineItem(skos);
        textItem.setNextProfilingLineItem(mbItem);
        initItem.setNextProfilingLineItem(textItem);
    }

    @Test
    public void test() throws MalformedURLException, ProfilingLineItemException {
        User user = new User();
        user.setUsername("dpalmisano");

        Activity a = new Activity();
        a.setVerb(Verb.TWEET);
        Tweet t = new Tweet();
        t.setUrl(new URL("http://twitter.com/dpalmisano/status/4637642"));
        t.setText("heading to the airport - leaving Rome for a couple of weeks");
        a.setObject(t);
        Context context = new Context();
        context.setDate(new DateTime());
        context.setService(new URL("http://twitter.com"));
        a.setContext(context);
        user.addActivity(a);

        Activity a2 = new Activity();
        a2.setVerb(Verb.LISTEN);
        Song s = new Song();
        s.setUrl(new URL("http://last.fm/song/4637642"));
        s.setMbid("244afcb7-fa9a-49b1-9aa6-0149512d1c52");
        a2.setObject(s);
        Context context2 = new Context();
        context2.setDate(new DateTime());
        context2.setService(new URL("http://last.fm"));
        a2.setContext(context2);
        user.addActivity(a2);

        Activity a3 = new Activity();
        a3.setVerb(Verb.TWEET);
        Tweet tweet2 = new Tweet();
        tweet2.setUrl(new URL("http://twitter.com/dpalmisano/status/66352"));
        tweet2.setText("Listening to the Thrice best album ever");
        a3.setObject(tweet2);
        Context context3 = new Context();
        context3.setDate(new DateTime());
        context3.setService(new URL("http://twitter.com"));
        a3.setContext(context3);
        user.addActivity(a3);

        Activity a4 = new Activity();
        a4.setVerb(Verb.TWEET);
        Tweet tweet3 = new Tweet();
        tweet3.setUrl(new URL("http://twitter.com/dpalmisano/status/11736"));
        tweet3.setText("Heading to Thriece gig tonight in London");
        a4.setObject(tweet2);
        Context context4 = new Context();
        context4.setDate(new DateTime());
        context4.setService(new URL("http://twitter.com"));
        a4.setContext(context4);
        user.addActivity(a4);

        initItem.execute(user);
    }

}
