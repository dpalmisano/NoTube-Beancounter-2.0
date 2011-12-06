package tv.notube.analytics.analysis;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class MethodDescription implements Serializable {

    private String name;

    private String[] parameterTypes;

    public MethodDescription(String name, String[] parameterTypes) {
        this.name = name;
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public void addParameter(String parameterType) {
        List<String> types = Arrays.asList(parameterTypes);
        types.add(parameterType);
        parameterTypes = types.toArray(new String[types.size()]);
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MethodDescription that = (MethodDescription) o;

        if (name != null ? !name.equals(that.name) : that.name != null)
            return false;
        if (!Arrays.equals(parameterTypes, that.parameterTypes))
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (parameterTypes != null ? Arrays.hashCode(parameterTypes) : 0);
        return result;
    }
}