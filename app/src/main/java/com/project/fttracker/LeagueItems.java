package com.project.fttracker;

public class LeagueItems {
    private final String name;
    private final String leagueId;
    private final String season;
    private final String flag;

    public LeagueItems(String name, String leagueId, String season, String flag) {
        this.name = name;
        this.leagueId = leagueId;
        this.season = season;
        this.flag = flag;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public String getSeason() {
        return season;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LeagueItems){
            LeagueItems item = (LeagueItems) obj;
            return this.name.equals(item.name) && this.leagueId.equals(item.leagueId);
        }
        return false;
    }
}
