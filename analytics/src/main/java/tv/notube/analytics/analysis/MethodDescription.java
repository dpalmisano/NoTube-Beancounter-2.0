package tv.notube.analytics.analysis;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.Arrays;

/**
 *
 */
public class MethodDescription implements Serializable {

    @Expose
    private String name;

    @Expose
    private String description;

    private String[] parameterTypes;

    public MethodDescription(
            String name,
            String description,
            String[] parameterTypes
    ) {
        this.name = name;
        this.description = description;
        this.parameterTypes = parameterTypes;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
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

    @Override
    public String toString() {
        return "MethodDescription{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", parameterTypes=" + (parameterTypes == null ? null : Arrays.asList(parameterTypes)) +
                '}';
    }
}