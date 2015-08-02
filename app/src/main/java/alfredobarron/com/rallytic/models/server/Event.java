package alfredobarron.com.rallytic.models.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Event {

    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private String description;
    @SerializedName("date_event")
    @Expose
    private String dateEvent;
    @SerializedName("hour_start")
    @Expose
    private String hourStart;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     * The name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return
     * The description
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     * The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     *
     * @return
     * The dateEvent
     */
    public String getDateEvent() {
        return dateEvent;
    }

    /**
     *
     * @param dateEvent
     * The date_event
     */
    public void setDateEvent(String dateEvent) {
        this.dateEvent = dateEvent;
    }

    /**
     *
     * @return
     * The hourStart
     */
    public String getHourStart() {
        return hourStart;
    }

    /**
     *
     * @param hourStart
     * The hour_start
     */
    public void setHourStart(String hourStart) {
        this.hourStart = hourStart;
    }

    /**
     *
     * @return
     * The createdAt
     */
    public String getCreatedAt() {
        return createdAt;
    }

    /**
     *
     * @param createdAt
     * The created_at
     */
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     *
     * @return
     * The updatedAt
     */
    public String getUpdatedAt() {
        return updatedAt;
    }

    /**
     *
     * @param updatedAt
     * The updated_at
     */
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}