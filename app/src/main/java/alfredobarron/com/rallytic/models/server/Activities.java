package alfredobarron.com.rallytic.models.server;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activities {

    @Expose
    private Integer id;
    @Expose
    private String name;
    @Expose
    private Integer time;
    @Expose
    private Integer intents;
    @Expose
    private String station;
    @Expose
    private String lat;
    @Expose
    private String lng;
    @SerializedName("question_id")
    @Expose
    private Integer questionId;
    @Expose
    private String question;
    @Expose
    private String answer;
    @Expose
    private String track;
    @Expose
    private Boolean penalty;

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
     * The time
     */
    public Integer getTime() {
        return time;
    }

    /**
     *
     * @param time
     * The time
     */
    public void setTime(Integer time) {
        this.time = time;
    }

    /**
     *
     * @return
     * The intents
     */
    public Integer getIntents() {
        return intents;
    }

    /**
     *
     * @param intents
     * The intents
     */
    public void setIntents(Integer intents) {
        this.intents = intents;
    }

    /**
     *
     * @return
     * The station
     */
    public String getStation() {
        return station;
    }

    /**
     *
     * @param station
     * The station
     */
    public void setStation(String station) {
        this.station = station;
    }

    /**
     *
     * @return
     * The lat
     */
    public String getLat() {
        return lat;
    }

    /**
     *
     * @param lat
     * The lat
     */
    public void setLat(String lat) {
        this.lat = lat;
    }

    /**
     *
     * @return
     * The lng
     */
    public String getLng() {
        return lng;
    }

    /**
     *
     * @param lng
     * The lng
     */
    public void setLng(String lng) {
        this.lng = lng;
    }

    /**
     *
     * @return
     * The questionId
     */
    public Integer getQuestionId() {
        return questionId;
    }

    /**
     *
     * @param questionId
     * The question_id
     */
    public void setQuestionId(Integer questionId) {
        this.questionId = questionId;
    }

    /**
     *
     * @return
     * The question
     */
    public String getQuestion() {
        return question;
    }

    /**
     *
     * @param question
     * The question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     *
     * @return
     * The answer
     */
    public String getAnswer() {
        return answer;
    }

    /**
     *
     * @param answer
     * The answer
     */
    public void setAnswer(String answer) {
        this.answer = answer;
    }

    /**
     *
     * @return
     * The track
     */
    public String getTrack() {
        return track;
    }

    /**
     *
     * @param track
     * The track
     */
    public void setTrack(String track) {
        this.track = track;
    }

    /**
     *
     * @return
     * The penalty
     */
    public Boolean getPenalty() {
        return penalty;
    }

    /**
     *
     * @param penalty
     * The penalty
     */
    public void setPenalty(Boolean penalty) {
        this.penalty = penalty;
    }

}