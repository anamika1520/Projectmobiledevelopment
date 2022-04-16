package com.project.fttracker;

public class FixtureItems {

    private final String  id;
    private String matchStatus;
    private String teamHome;
    private String teamAway;
    private String league;

    public FixtureItems(String id, String matchStatus, String teamHome, String teamAway, String league) {
        this.matchStatus = matchStatus;
        this.teamHome = teamHome;
        this.teamAway = teamAway;
        this.league = league;
        this.id = id;
    }


    public String getMatchStatus() {
        return matchStatus;
    }

    public void setMatchStatus(String matchStatus) {
        this.matchStatus = matchStatus;
    }

    public String getTeamHome() {
        return teamHome;
    }

    public void setTeamHome(String teamHome) {
        this.teamHome = teamHome;
    }

    public String getTeamAway() {
        return teamAway;
    }

    public void setTeamAway(String teamAway) {
        this.teamAway = teamAway;
    }

    public String getLeague() {
        return league;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getId() {
        return id;
    }
}
