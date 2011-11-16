package tv.notube.commons.alog;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Query {

    private static final String INTEGER_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value %s %s ";

    private static final String STRING_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value = '%s' ";

    private static final String URL_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value = '%s' ";

    private static final String DATETIME_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value %s '%s' ";

    public enum Math {
        GT,
        EQ,
        LT
    }

    public enum Boolean {
        AND,
        OR
    }

    private boolean complete = false;

    private Object[] stack = new Object[2];

    private int index = 0;

    public void push(Field field, Math operator) {
        if(complete) {
            throw new IllegalStateException("The query is not well-formed");
        }
        stack[index] = field;
        stack[index + 1] = operator;
        complete = true;
    }

    public void push(Boolean bool) {
        if(!complete) {
            throw new IllegalStateException("The query is not well-formed");
        }
        stack = expand();
        stack[index] = bool;
        index++;
        complete = false;
    }

    public boolean isComplete() {
        return complete;
    }

    public String compile() {
        if(!complete) {
            throw new IllegalStateException("The query is not well-formed");
        }
        String query = "";
        for(int i = 0; i < stack.length; i++) {
            Object obj = stack[i];
            if(obj instanceof StringField) {
                StringField sf = (StringField) obj;
                query += String.format(
                        STRING_FIELD_PATTERN,
                        "string",
                        sf.getName(),
                        "string",
                        sf.getValue()
                );
            } else if(obj instanceof IntegerField) {
                IntegerField intf = (IntegerField) obj;
                query += String.format(
                        INTEGER_FIELD_PATTERN,
                        "integer",
                        intf.getName(),
                        "integer",
                        getMathOperator((Math) stack[i+1]),
                        intf.getValue()
                );
            } else if(obj instanceof URLField) {
                URLField urlf = (URLField) obj;
                query += String.format(
                        URL_FIELD_PATTERN,
                        "url",
                        urlf.getName(),
                        "url",
                        urlf.getValue().toString()
                );
            } else if(obj instanceof DatetimeField) {
                DatetimeField dtf = (DatetimeField) obj;
                query += String.format(
                        DATETIME_FIELD_PATTERN,
                        "datetime",
                        dtf.getName(),
                        "datetime",
                        getMathOperator((Math) stack[i+1]),
                        dtf.getValue().getMillis()
                );
            } else if(obj instanceof Boolean) {
                query += obj + " ";
            }
        }
        return query.trim();
    }

    private char getMathOperator(Math math) {
        if(math.equals(Math.EQ)) {
            return '=';
        }
        if(math.equals(Math.GT)) {
            return '>';
        }
        if(math.equals(Math.LT)) {
            return '<';
        }
        throw new IllegalArgumentException("Math operator '" + math + "' is " +
                "not supported");
    }

    private Object[] expand() {
        Object newStack[] = new Object[stack.length + 3];
        index = index + 2;
        int i = 0;
        while(i < index) {
            newStack[i] = stack[i];
            i++;
        }
        return newStack;
    }

}
