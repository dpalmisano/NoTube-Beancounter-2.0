package tv.notube.kvs.storage;

import tv.notube.kvs.storage.configuration.ConfigurationManager;
import tv.notube.kvs.storage.mybatis.MyBatisKVStore;
import tv.notube.kvs.storage.serialization.SerializationManager;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class MyBatisKVStoreTestCase {

    private final static String CONFIG_FILE =
            "tv/notube/kv/configuration/kvs-configuration.xml";

    private final static String TABLE = "test";

    private KVStore kVStore;

    private SecureRandom random;

    @BeforeTest
    public void setUp() {
        Properties properties = ConfigurationManager.getInstance(CONFIG_FILE)
                .getKVStoreConfiguration()
                .getProperties();
        kVStore = new MyBatisKVStore(properties, new SerializationManager());
        random = new SecureRandom();
    }

    @AfterTest
    public void tearDown() {
        kVStore = null;
    }

    @Test
    public void testCRUD() throws MalformedURLException, KVStoreException {
        TestClass expected = new TestClass();
        expected.setBool(false);
        expected.setString("a test string");
        expected.setUrl(new URL("http://google.com"));

        Field field = new Field();
        field.setName("date");
        field.setValue((new Date()).toString());

        final String key = UUID.randomUUID().toString();
        kVStore.setValue(TABLE, key, expected, field);

        TestClass actual = (TestClass) kVStore.getValue(TABLE, key);
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual, expected);
        Assert.assertEquals(actual.getUrl(), expected.getUrl());

        Field[] fields = kVStore.getFields(TABLE, key);
        Assert.assertNotNull(fields);
        Assert.assertEquals(fields.length, 1);
        Assert.assertEquals(fields[0], field);
        List<String> keys = kVStore.search(TABLE, KVStore.Boolean.AND, field);
        Assert.assertNotNull(keys);
        Assert.assertEquals(keys.size(), 1);

        kVStore.deleteValue(TABLE, key);
        actual = (TestClass) kVStore.getValue(TABLE, key);
        Assert.assertNull(actual);

        fields = kVStore.getFields(TABLE, key);
        Assert.assertEquals(fields.length, 0);
    }

    @Test
    public void testCollectionWithMultipleParametersSimpleCRUD()
            throws MalformedURLException, KVStoreException {
        List<TestClass> expected = new ArrayList<TestClass>();
        final int SIZE = 1000;
        for (int i = 0; i < SIZE; i++) {
            TestClass tc = new TestClass();
            tc.setUrl(new URL("http://test.com/" + i));
            tc.setString(new BigInteger(130, random).toString(32));
            expected.add(tc);
        }
        Field field1 = new Field();
        field1.setName("date");
        field1.setValue((new Date().toString()));
        Field field2 = new Field();
        field2.setName("author");
        field2.setValue("Davide Palmisano");
        String id = UUID.randomUUID().toString();
        kVStore.setValue(TABLE, id, expected, field1, field2);

        List<TestClass> actual = (List<TestClass>) kVStore.getValue(TABLE, id);
        Assert.assertNotNull(actual);
        Assert.assertEquals(actual.size(), SIZE);

        kVStore.deleteValue(TABLE, id);
        Assert.assertNull(kVStore.getValue(TABLE, id));
    }


    @Test
    public void testOrdering() throws MalformedURLException, KVStoreException {
        TestClass tc1 = new TestClass();
        tc1.setUrl(new URL("http://test.com/smaller"));
        tc1.setString("smaller");

        Field f11 = new Field();
        f11.setName("millis");
        f11.setValue("150");

        kVStore.setValue(TABLE, "key1", tc1, f11);

        TestClass tc2 = new TestClass();
        tc2.setUrl(new URL("http://test.com/bigger"));
        tc2.setString("bigger");

        Field f21 = new Field();
        f21.setName("millis");
        f21.setValue("50");

        kVStore.setValue(TABLE, "key2", tc2, f21);

        Field q = new Field();
        q.setName("millis");
        q.setValue("100");

        List<String> actuals = kVStore.search(TABLE, KVStore.Math.LESS, q);
        Assert.assertEquals(actuals.size(), 1);
        Assert.assertEquals(actuals.get(0), "key2");

        actuals = kVStore.search(TABLE, KVStore.Math.GREAT, q);
        Assert.assertEquals(actuals.size(), 1);
        Assert.assertEquals(actuals.get(0), "key1");

        kVStore.deleteValue(TABLE, "key1");
        kVStore.deleteValue(TABLE, "key2");

        Assert.assertNull(kVStore.getValue(TABLE, "key1"));
        Assert.assertNull(kVStore.getValue(TABLE, "key2"));
    }

    @Test
    public void testEquality() throws MalformedURLException, KVStoreException {
        TestClass tc1 = new TestClass();
        tc1.setUrl(new URL("http://test.com/1"));
        tc1.setString("one");

        Field f11 = new Field();
        f11.setName("name");
        f11.setValue("one");

        kVStore.setValue(TABLE, "key1", tc1, f11);

        TestClass tc2 = new TestClass();
        tc2.setUrl(new URL("http://test.com/2"));
        tc2.setString("two");

        Field f12 = new Field();
        f12.setName("name");
        f12.setValue("two");

        kVStore.setValue(TABLE, "key2", tc2, f12);

        List<String> actualKeys = kVStore.search(
                TABLE,
                KVStore.Math.EQUALS,
                f11
        );

        Assert.assertNotNull(actualKeys);
        Assert.assertEquals(1, actualKeys.size());
        Assert.assertEquals("key1", actualKeys.get(0));
        kVStore.deleteValue(TABLE, "key1");
        Assert.assertNull(kVStore.getValue(TABLE, "key1"));
    }

}
