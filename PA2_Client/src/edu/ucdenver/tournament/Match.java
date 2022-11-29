package edu.ucdenver.tournament;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Match {
    private LocalDate dateTime;
    private int scoreTeamA;
    private int scoreTeamB;

    private List<Referee> matchReferees;
    private LineUp teamA;
    private LineUp teamB;

    public Match(LocalDate dateTime, Team teamA, Team teamB){
        this.dateTime = dateTime;
        this.matchReferees = new ArrayList<>();
        this.teamA = new LineUp(teamA);
        this.teamB = new LineUp(teamB);
    }

    public LocalDate getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDate dateTime) {
        this.dateTime = dateTime;
    }

    public int getScoreTeamA() {
        return scoreTeamA;
    }

    public void setScoreTeamA(int scoreTeamA) {
        this.scoreTeamA = scoreTeamA;
    }

    public int getScoreTeamB() {
        return scoreTeamB;
    }

    public void setScoreTeamB(int scoreTeamB) {
        this.scoreTeamB = scoreTeamB;
    }


    public LineUp getTeamA() {
        return teamA;
    }

    public void setTeamA(Team teamA) {
        this.teamA = new LineUp(teamA);
    }

    public LineUp getTeamB() {
        return teamB;
    }

    public void setTeamB(Team teamB) {
        this.teamB = new LineUp(teamB);
    }

    public boolean isUpcoming(){
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());
        return dateTime.isAfter(localDate);
    }

    public void addPlayer(Player player, Team team){
        if(team.equals(teamA.getTeam())){
            teamA.addPlayer(player);
        }
        else if(team.equals(teamB.getTeam())){
            teamB.addPlayer(player);
        }
    }

    public List<Referee> getReferees(){
        return this.matchReferees;
    }

    public void addReferee(Referee referee){
        this.matchReferees.add(referee);
    }

    public void setMatchScore(int scoreTeamA, int scoreTeamB){
        this.scoreTeamA = scoreTeamA;
        this.scoreTeamB = scoreTeamB;
    }

    @Override
    public String toString(){
        return String.format("\nMatch Date: %s \nTeam A: %s \nTeam B: %s \n", this.dateTime.toString(),
                this.teamA, this.teamB);
    }
}
