package app.domains;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.Objects;

public class PropertyInfo {
    private String id;
    private String ownerId;
    private String location;
    private String description;
    private String name;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime updatedDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PropertyInfo propertyInfo = (PropertyInfo) o;
        return Objects.equals(id, propertyInfo.id) &&
                Objects.equals(ownerId, propertyInfo.ownerId) &&
                Objects.equals(location, propertyInfo.location) &&
                Objects.equals(description, propertyInfo.description) &&
                Objects.equals(name, propertyInfo.name) &&
                Objects.equals(createdDate, propertyInfo.createdDate) &&
                Objects.equals(updatedDate, propertyInfo.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ownerId, location, description, name, createdDate, updatedDate);
    }
}
