package tv.notube.commons.alog.mapper;

import org.joda.time.DateTime;
import tv.notube.commons.alog.fields.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class BeanMapper<T> {

    private static String SET = "set%s";

    private static String GET = "get%s";

    private static Class[] types = {
            String.class,
            DateTime.class,
            URL.class,
            Integer.class
    };

    public T toObject(Field fields[], Class<? extends T> type) throws
            BeanMapperException {
        Object object;
        try {
            object = type.newInstance();
        } catch (InstantiationException e) {
            throw new BeanMapperException(
                    "Error while instantiating type object",
                    e
            );
        } catch (IllegalAccessException e) {
            throw new BeanMapperException(
                    "Error while accessing type object constructor",
                    e
            );
        }
        for (Field field : fields) {
            String setterName = getSetterName(field);
            Method setter;
            try {
                setter = type.getMethod(
                        setterName,
                        field.getValue().getClass()
                );
            } catch (NoSuchMethodException e) {
                throw new BeanMapperException("Setter for field '" + field
                        .getName() + "' has not found", e);
            }
            try {
                setter.invoke(object, field.getValue());
            } catch (IllegalAccessException e) {
                throw new BeanMapperException(
                        "Error while accessing setter",
                        e);
            } catch (InvocationTargetException e) {
                throw new BeanMapperException(
                        "Error while invoking setter",
                        e);
            }
        }
        return (T) object;
    }

    private Class<Object> getSetterType(Class<Object> aClass) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    private String getSetterName(Field field) {
        String name = field.getName();
        name = name.substring(0, 1).toUpperCase() + name.substring(1, name.length());
        return String.format(SET, name);
    }

    public Field[] toFields(T object) throws BeanMapperException {
        Class clazz = object.getClass();
        Method getters[] = getGetters(clazz);
        List<Field> fields = new ArrayList<Field>();
        for (Method getter : getters) {
            Object valueObj;
            try {
                valueObj = getter.invoke(object);
            } catch (IllegalAccessException e) {
                throw new BeanMapperException(
                        "Error while accessing getter '" + getter.getName() + "'",
                        e
                );
            } catch (InvocationTargetException e) {
                throw new BeanMapperException(
                        "Error while invoking getter '" + getter.getName() + "'",
                        e
                );
            }
            Class fieldClass = getFieldClass(getter.getReturnType());
            if (fieldClass == null) {
                // there is no mappable field. just skip.
                continue;
            }
            Constructor fieldConstructor;
            try {
                fieldConstructor = fieldClass.getConstructor(
                        String.class,
                        getter.getReturnType()
                );
            } catch (NoSuchMethodException e) {
                throw new BeanMapperException(
                        "Error while invoking field constructor",
                        e
                );
            }
            try {
                fields.add((Field)
                        fieldConstructor.newInstance(
                                getName(getter),
                                valueObj
                        )
                );
            } catch (InstantiationException e) {
                throw new BeanMapperException(
                        "Error while instantiating field",
                        e
                );
            } catch (IllegalAccessException e) {
                throw new BeanMapperException(
                        "Error while accessing field",
                        e
                );
            } catch (InvocationTargetException e) {
                throw new BeanMapperException(
                        "Error while invoking field constructor",
                        e
                );
            }
        }
        return fields.toArray(new Field[fields.size()]);
    }

    private String getName(Method getter) {
        String getterName = getter.getName();
        return getterName.substring(3, getterName.length()).toLowerCase();
    }

    private Class getFieldClass(Class<?> returnType) {
        if (returnType.equals(String.class)) {
            return StringField.class;
        }
        if (returnType.equals(Integer.class) || returnType.equals(int.class)) {
            return IntegerField.class;
        }
        if (returnType.equals(URL.class)) {
            return URLField.class;
        }
        if (returnType.equals(DateTime.class)) {
            return DatetimeField.class;
        }
        return null;
    }

    private Class<?> getParameterClass(Class clazz) {
        if (clazz.equals(StringField.class)) {
            return String.class;
        }
        if (clazz.equals(IntegerField.class)) {
            return Integer.class;
        }
        if (clazz.equals(URLField.class)) {
            return URL.class;
        }
        if (clazz.equals(DatetimeField.class)) {
            return DateTime.class;
        }
        return null;
    }

    private Method[] getGetters(Class clazz) {
        List<Method> getters = new ArrayList<Method>();
        for (Method candidateGetter : clazz.getDeclaredMethods()) {
            if (candidateGetter.getName().startsWith("get")) {
                getters.add(candidateGetter);
            }
        }
        return getters.toArray(new Method[getters.size()]);
    }

    private Method[] getSetters(Class clazz) {
        List<Method> setters = new ArrayList<Method>();
        for (Method candidateSetter : clazz.getDeclaredMethods()) {
            if (candidateSetter.getName().startsWith("set")) {
                setters.add(candidateSetter);
            }
        }
        return setters.toArray(new Method[setters.size()]);
    }

}
