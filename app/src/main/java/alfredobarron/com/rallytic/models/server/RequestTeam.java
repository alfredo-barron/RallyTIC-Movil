package alfredobarron.com.rallytic.models.server;

import com.google.gson.annotations.Expose;

public class RequestTeam {

    @Expose
    private Integer status;
    @Expose
    private Team team;

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
     * The team
     */
    public Team getTeam() {
        return team;
    }

    /**
     *
     * @param team
     * The team
     */
    public void setTeam(Team team) {
        this.team = team;
    }

}
