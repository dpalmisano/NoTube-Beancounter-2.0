package tv.notube.commons.storage.kvs.serialization;

import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class SerializationManagerTestCase {

    SerializationManager serializationManager;

    @BeforeTest
    public void setUp() {
        serializationManager = new SerializationManager();
    }

    @AfterTest
    public void tearDown() {
        serializationManager = null;
    }

    @Test
    public void testSimpleSerializationAndDeserialization()
            throws SerializationManagerException, URISyntaxException, IOException {
        TestClassToBeSerialized expected = new TestClassToBeSerialized();
        expected.addURI(new URI("http://davidepalmisano.com"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        serializationManager.serialize(expected, baos);
        final byte[] serialization = baos.toByteArray();
        baos.close();

        TestClassToBeSerialized actual;
        InputStream is = new ByteArrayInputStream(serialization);
        actual = (TestClassToBeSerialized) serializationManager.deserialize(is);
        Assert.assertEqualsNoOrder(expected.getURIs(), actual.getURIs());
        is.close();
    }

}
