package tv.notube.commons.alog;

import org.testng.Assert;
import org.testng.annotations.Test;
import tv.notube.commons.alog.fields.IntegerField;
import tv.notube.commons.alog.fields.StringField;

/**
 * Unit test for simple App.
 */
public class QueryTestCase {

    @Test
    public void testCompile() {
        final String expected = "stringfield.name = 'name' AND stringfield" +
                ".value = 'Davide' OR integerfield.name = 'age' AND " +
                "integerfield.value > 31";
        Query query = new Query();
        StringField field1 = new StringField("name", "Davide");
        IntegerField field2 = new IntegerField("age", 31);
        query.push(field1, Query.Math.EQ);
        query.push(Query.Boolean.OR);
        query.push(field2, Query.Math.GT);
        Assert.assertEquals(query.compile(), expected);
    }

}
