package tv.notube.commons.storage.model;

import tv.notube.commons.storage.model.fields.*;

import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Query implements Serializable {

    static final long serialVersionUID = 10278539172337475L;

    private static final String INTEGER_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value %s %s ";

    private static final String STRING_FIELD_PATTERN = "%sfield.name = '%s' " +
            "AND %sfield.value %s '%s' ";

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

    public static void decompile(
            String query,
            Query queryObj
    ) throws QueryException {
        // space*{string, integer, datetime, url}.<field-name>space*{=, <,>}space*<value>space*{AND, OR}
        // eg: string.verb = TWEET OR string.verb = LISTEN OR string.verb = WATCHED
        if(query == null) {
            throw new IllegalArgumentException("query parameter cannot be null");
        }
        String q = query;
        if(q.length() == 0) {
            return;
        }
        q = eat(q, ' ');
        if (q.startsWith("AND") || q.startsWith("OR")) {
            String boolOp = dump(q, ' ');
            queryObj.push(Boolean.valueOf(boolOp));
            q = q.substring(boolOp.length(), q.length());
            decompile(q, queryObj);
            return;
        }
        String fieldType = dump(q, '.');
        q = q.substring(fieldType.length(), q.length());
        q = eat(q, '.');
        String fieldName = dump(q, ' ');
        q = q.substring(fieldName.length(), q.length());
        q = eat(q, ' ');
        String mathOpStr = dump(q, ' ');
        Math op = consumeMathOp(mathOpStr);
        q = q.substring(mathOpStr.length(), q.length());
        q = eat(q, ' ');
        String fieldValue = dump(q, ' ');
        q = q.substring(fieldValue.length(), q.length());
        queryObj.push(getField(fieldType, fieldName, fieldValue), op);
        decompile(q, queryObj);
    }

    private static <T> Field<T> getField(
            String fieldType,
            String fieldName,
            T fieldValue
    ) throws QueryException {
        char c = fieldType.toUpperCase().charAt(0);
        String fieldClassName = c + fieldType.substring(1);
        Class fieldTypeClass;
        try {
            fieldTypeClass = Class.forName("tv.notube.commons.storage.model" +
                    ".fields." + fieldClassName + "Field");
        } catch (ClassNotFoundException e) {
            throw new QueryException();
        }
        Constructor<Field<T>> constructor;
        Class valueType;
        try {
            valueType = Class.forName("java.lang." + fieldClassName);
        } catch (ClassNotFoundException e) {
            throw new QueryException();
        }
        try {
            constructor = fieldTypeClass.getConstructor(String.class, valueType);
        } catch (NoSuchMethodException e) {
            throw new QueryException();
        }
        Constructor valueTypeC;
        try {
            valueTypeC = valueType.getConstructor(fieldValue.getClass());
        } catch (NoSuchMethodException e) {
            throw new QueryException();
        }
        Field field;
        try {
            field = constructor.newInstance(fieldName, valueTypeC.newInstance(fieldValue));
        } catch (InstantiationException e) {
            throw new QueryException();
        } catch (IllegalAccessException e) {
            throw new QueryException();
        } catch (InvocationTargetException e) {
            throw new QueryException();
        }
        return field;
    }

    private static String dump(String string, char c) {
        int i = 0;
        while (i < string.length()) {
            if (string.charAt(i) == c) {
                return string.substring(0, i);
            }
            i++;
        }
        return string;
    }

    private static String eat(String string, char c) {
        if (string.length() == 0) {
            return string;
        }
        if (string.charAt(0) != c) {
            return string;
        }
        int i = 0;
        while (i < string.length()) {
            if (string.charAt(i) == c) {
                i++;
                continue;
            }
            return string.substring(i, string.length());
        }
        return "";
    }

    private static Math consumeMathOp(String query) throws QueryException {
        query = query.trim();
        char startsWith = query.charAt(0);
        if (startsWith == '=') {
            return Math.EQ;
        }
        if (startsWith == '>') {
            return Math.GT;
        }
        if (startsWith == '<') {
            return Math.LT;
        }
        throw new QueryException();
    }

    private boolean complete = false;

    private Object[] stack = new Object[2];

    private int index = 0;

    public void push(Field field, Math operator) {
        if (complete) {
            throw new IllegalStateException("The query is not well-formed");
        }
        stack[index] = field;
        stack[index + 1] = operator;
        complete = true;
    }

    public void push(Boolean bool) {
        if (!complete) {
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
        if (!complete) {
            throw new IllegalStateException("The query is not well-formed");
        }
        String query = "";
        for (int i = 0; i < stack.length; i++) {
            Object obj = stack[i];
            if (obj instanceof StringField) {
                StringField sf = (StringField) obj;
                query += String.format(
                        STRING_FIELD_PATTERN,
                        "string",
                        sf.getName(),
                        "string",
                        getMathOperator((Math) stack[i + 1]),
                        sf.getValue()
                );
            } else if (obj instanceof IntegerField) {
                IntegerField intf = (IntegerField) obj;
                query += String.format(
                        INTEGER_FIELD_PATTERN,
                        "integer",
                        intf.getName(),
                        "integer",
                        getMathOperator((Math) stack[i + 1]),
                        intf.getValue()
                );
            } else if (obj instanceof URLField) {
                URLField urlf = (URLField) obj;
                query += String.format(
                        URL_FIELD_PATTERN,
                        "url",
                        urlf.getName(),
                        "url",
                        urlf.getValue().toString()
                );
            } else if (obj instanceof DatetimeField) {
                DatetimeField dtf = (DatetimeField) obj;
                query += String.format(
                        DATETIME_FIELD_PATTERN,
                        "datetime",
                        dtf.getName(),
                        "datetime",
                        getMathOperator((Math) stack[i + 1]),
                        dtf.getValue().getMillis()
                );
            } else if (obj instanceof Boolean) {
                query += obj + " ";
            }
        }
        return query.trim();
    }

    private char getMathOperator(Math math) {
        if (math.equals(Math.EQ)) {
            return '=';
        }
        if (math.equals(Math.GT)) {
            return '>';
        }
        if (math.equals(Math.LT)) {
            return '<';
        }
        throw new IllegalArgumentException("Math operator '" + math + "' is " +
                "not supported");
    }

    private Object[] expand() {
        Object newStack[] = new Object[stack.length + 3];
        index = index + 2;
        int i = 0;
        while (i < index) {
            newStack[i] = stack[i];
            i++;
        }
        return newStack;
    }

}
