package tv.notube.platform;

import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Service {

    private static class Param<T> {

        private Class<T> clazz;

        private String name;

        private T value;

        public Param(Class<T> clazz, String name, T value) {
            this.clazz = clazz;
            this.name = name;
            this.value = value;
        }

        public boolean isNull() {
            return this.value == null;
        }

        public boolean hasValue(T value) {
            if (isNull()) {
                throw new IllegalStateException(
                        "this parameter is null-valued"
                );
            }
            if (this.value.equals(value)) {
                return true;
            }
            return false;
        }

        public Class<T> getClazz() {
            return clazz;
        }

        public String getName() {
            return name;
        }

        public T getValue() {
            return value;
        }
    }

    private static void areCompliant(Param... params) throws ServiceException {
        if (params.length == 0) {
            throw new ServiceException(
                    "Parameters are all null"
            );
        }
        for (Param p : params) {
            if (p.isNull()) {
                throw new ServiceException(
                        "Parameter " + p.getName() + " is null"
                );
            }
            if (p.getClazz().equals(String.class)) {
                String value = (String) p.getValue();
                if (value.equals("")) {
                    throw new ServiceException(
                            "Parameter " + p.getName() + " cannot be empty string"
                    );
                }
            }
        }
    }

    public static void check(Class clazz, String name, Object... values) throws ServiceException {
        Method m;
        Class<?>[] signature = getSignature(values);
        try {
            m = clazz.getMethod(
                    name,
                    signature
            );
        } catch (NoSuchMethodException e) {
            throw new ServiceException("Method not found", e);
        }
        check(m, values);
    }

    private static Class<?>[] getSignature(Object[] values) {
        Class<?>[] signature = new Class<?>[values.length];
        int i = 0;
        for(Object o : values) {
            if(o == null) {
                // let's assume it's a String
                signature[i] = String.class;
                i++;
                continue;
            }
            signature[i] = o.getClass();
            i++;
        }
        return signature;
    }

    private static void check(Method m, Object... values) throws
            ServiceException {
        Map<String, Class> types = getJerseyAnnotatedParameters(m);
        int i = 0;
        String names[] = types.keySet().toArray(new String[types.keySet().size()]);
        List<Param> params = new ArrayList<Param>();
        for (Object value : values) {
            Param p = new Param(types.get(names[i]), names[i], value);
            params.add(p);
            i++;
        }
        areCompliant(params.toArray(new Param[params.size()]));
    }

    private static Map<String, Class> getJerseyAnnotatedParameters(Method m) {
        Annotation[][] annotations = m.getParameterAnnotations();
        Class<?> types[] = m.getParameterTypes();
        Map<String, Class> result = new HashMap<String, Class>();
        int i = 0;
        for (Annotation a[] : annotations) {
            for (Annotation aa : a) {
                if (aa.annotationType().equals(QueryParam.class) || aa.annotationType().equals(PathParam.class)) {
                    result.put(aa.toString(), types[i]);
                }
            }
            i++;
        }
        return result;
    }


}
