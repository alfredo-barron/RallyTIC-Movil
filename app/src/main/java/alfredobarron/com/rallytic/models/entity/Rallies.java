package alfredobarron.com.rallytic.models.entity;

import io.realm.RealmObject;

/**
 * Created by alfredobarron on 27/07/15.
 */
public class Rallies extends RealmObject {

    private int id;

    private String name;

    private String description;

    private int score;

    private boolean completed;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
}
