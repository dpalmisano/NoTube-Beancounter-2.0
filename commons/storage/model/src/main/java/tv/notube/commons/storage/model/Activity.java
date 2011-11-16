package tv.notube.commons.storage.model;

import org.joda.time.DateTime;

import java.util.UUID;

/**
 * put class description here
 *
 * @author Davide Palmisano ( dpalmisano@gmail.com )
 */
public class Activity {

    private UUID id;

    private DateTime dateTime;

    private String owner;

    private String description;

    public Activity(UUID id, DateTime dateTime, String owner, String description) {
        this.id = id;
        this.dateTime = dateTime;
        this.owner = owner;
        this.description = description;
    }

    public Activity(String owner, String description) {
        this.id = UUID.randomUUID();
        this.dateTime = new DateTime();
        this.owner = owner;
        this.description = description;
    }

    public UUID getId() {
        return id;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public String getOwner() {
        return owner;
    }

    public String getDescription() {
        return description;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Activity activity = (Activity) o;

        if (id != null ? !id.equals(activity.id) : activity.id != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Activity{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", owner='" + owner + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
