package tv.notube.commons.alog;

/**
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public abstract class Field<T> {

    private String name;

    private T value;

    public Field(String name, T value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Field field = (Field) o;

        if (name != null ? !name.equals(field.name) : field.name != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Field{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }
}
