package tv.notube.commons.skos.service;

import junit.framework.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SkosTestCase {

    private Skos skos;

    @BeforeTest
    public void setUp() {
        skos = new Skos();
    }

    @AfterTest
    public void tearDown() {
        skos = null;
    }

    @Test
    public void testSkos() throws SkosException, URISyntaxException {
        List<URI> actual = skos.getSkos(new URI("http://dbpedia.org/resource/Bassiano"));
        Assert.assertEquals(1, actual.size());
        Assert.assertEquals(
                actual.get(0),
                new URI("http://dbpedia.org/resource/Category:Cities_and_towns_in_the_Lazio")
        );
    }

}
