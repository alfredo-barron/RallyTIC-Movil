package alfredobarron.com.rallytic.models.server;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;

import alfredobarron.com.rallytic.models.server.Activities;
import alfredobarron.com.rallytic.models.server.Event;

public class Rally {

    @Expose
    private Integer status;
    @Expose
    private Event event;
    @Expose
    private List<Activities> activities = new ArrayList<Activities>();

    /**
     *
     * @return
     * The status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     *
     * @param status
     * The status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     *
     * @return
     * The event
     */
    public Event getEvent() {
        return event;
    }

    /**
     *
     * @param event
     * The event
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     *
     * @return
     * The activities
     */
    public List<Activities> getActivities() {
        return activities;
    }

    /**
     *
     * @param activities
     * The activities
     */
    public void setActivities(List<Activities> activities) {
        this.activities = activities;
    }

}