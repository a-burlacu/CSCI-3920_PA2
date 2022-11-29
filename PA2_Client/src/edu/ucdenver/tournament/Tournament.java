package edu.ucdenver.tournament;

import java.io.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class Tournament implements Serializable{
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    private List<Match> listMatches;
    private List<Team> listTeams;
    private List<Referee> listReferees;
    private List<Country> participatingCountries;

    //--------------------------------------------------
    //                  constructors
    //--------------------------------------------------
    public Tournament(String name, LocalDate startDate, LocalDate endDate){
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;

        this.listMatches = new ArrayList<>();
        this.listTeams = new ArrayList<>();
        this.listReferees = new ArrayList<>();
        this.participatingCountries = new ArrayList<>();
    }


    //--------------------------------------------------
    //                    getters
    //--------------------------------------------------

    public List<Match> getListMatches(){
        return listMatches;
    }

    public List<Team> getListTeams(){
        return listTeams;
    }

    public List<Referee> getListReferees(){
        return listReferees;
    }

    public List<Country> getParticipatingCountries(){
        return participatingCountries;
    }

    public String getName() {
        return name;
    }

    //--------------------------------------------------
    //                    setters
    //--------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }


    //--------------------------------------------------
    //                  class methods
    //--------------------------------------------------

    public static Tournament loadFromFile(String filename){
        ObjectInputStream ois = null;
        Tournament tournament = null;

        try{
            ois = new ObjectInputStream(new FileInputStream(filename));
            tournament = (Tournament) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
            tournament = new Tournament(null, null, null);
        }
        finally{
            if (ois != null){
                try{
                    ois.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
        return tournament;
    }

    public void saveToFile(String filename){
        ObjectOutputStream oos = null;

        try{
            oos = new ObjectOutputStream(new FileOutputStream(filename));
            oos.writeObject(this);
        }
        catch (IOException ioe){
            ioe.printStackTrace();
        }
        finally {
            if (oos != null){
                try{
                    oos.close();
                }
                catch (IOException ioe){
                    ioe.printStackTrace();
                }
            }
        }
    }

    public void addCountry(String countryName) throws IllegalArgumentException{
        //creating a Country object
        Country country = null;

        //checking to see if the country already exists in the tournament
        for(Country c : participatingCountries){
            if(c.getCountryName().equalsIgnoreCase(countryName)){
                throw new IllegalArgumentException("The country '" + countryName + "' already exists.");
            }
        }

        //if the country does not exist, add it
        country = new Country(countryName);
        this.participatingCountries.add(country);
    }

    public void addTeam(String name, String countryName) throws IllegalArgumentException{
        //Creating country and team objects
        Country country = null;
        Team team = null;

        //checking if the team already exists in the tournament
        for(Team t : listTeams){
            if(t.getName().equalsIgnoreCase(name)){
                throw new IllegalArgumentException("The team '" + name + "' already exists.");
            }
        }

        //checking if the country already exists in the tournament
        for (Country c : participatingCountries){
            if(c.getCountryName().equalsIgnoreCase(countryName)){
                country = c;
            }
        }

        //if the country already exists in the tournament, add the new team
        if(country != null) {
            team = new Team(name, country);
            this.listTeams.add(team);
        }
        //if the country does not exist in the tournament, throw an error
        else if(country == null){
            throw new IllegalArgumentException("The country '" + countryName + "' does not exist.");
        }
    }

    public void addReferee(String name, String countryName) throws IllegalArgumentException{
        //creating country and referee objects
        Country country = null;
        Referee referee = null;

        //checking to see if the referee already exists in the tournament
        for (Referee r : listReferees){
            if(r.getName().equalsIgnoreCase(name)){
                throw new IllegalArgumentException("The referee '" + name + "' already exists.");
            }
        }

        //checking to see if the country already exists in the tournament
        for (Country c : participatingCountries){
            if(c.getCountryName().equalsIgnoreCase(countryName)){
                country = c;
            }
        }

        //if the country exists but the referee does not, add the new referee
        if(referee == null && country != null){
            referee = new Referee(name, country);
            this.listReferees.add(referee);
        }

        //if the country does not exist, throw an error
        else if (country == null){
            throw new IllegalArgumentException("The country '" + countryName + "' does not exist.");
        }
    }

    public void addPlayer(String teamName, String playerName, int age, double height, double weight)
            throws IllegalArgumentException{
        //creating a Team object
        Team team = null;
        Player player = null;

        //checking to see if the team already exists in the tournament
        for (Team t : listTeams){
            if(t.getName().equalsIgnoreCase(teamName)){
                team = t;
            }
        }

        //if the team exists, add the player
        if(team != null) {
            //checking to see if the player already exists in the tournament
            for (Player p : team.getSquad()){
                if(p.getName().equalsIgnoreCase(playerName)){
                    throw new IllegalArgumentException("The player '" + playerName + "' already exists.");
                }
            }
            team.addPlayer(playerName, age, height, weight);
        }
        else if(team == null){
            throw new IllegalArgumentException("The team '" + teamName + "' does not exist.");
        }
    }

    public void addMatch(LocalDate dateTime, String teamAName, String teamBName) throws IllegalArgumentException{
        //creating two Team objects, one for team A and one for team B
        Team teamA = null;
        Team teamB = null;

        if(teamAName == teamBName){
            throw new IllegalArgumentException("Team A and Team B cannot be the same.");
        }

        //checking to see if team A and team B exist in the tournament
        for (Team t : listTeams){
            if(t.getName().equalsIgnoreCase(teamAName)){
                teamA = t;
            }
            if(t.getName().equalsIgnoreCase(teamBName)){
                teamB = t;
            }
        }

        //if the teams exist in the tournament, add the match
        if(teamA != null && teamB != null){
            Match match = null;

            //checking to see if the match already exists in the tournament and throwing an error if it does
            for (Match m: listMatches){
                if(m.getDateTime().equals(dateTime)){
                    throw new IllegalArgumentException("This match already exists");
                }
            }

            //adding match if it does not already exist
            match = new Match(dateTime, teamA, teamB);
            listMatches.add(match);
        }
        else if(teamA == null || teamB == null){
            throw new IllegalArgumentException("One of the teams entered does not exist.");
        }
    }

    public void addRefereeToMatch(LocalDate dateTime, String refereeName) throws IllegalArgumentException{
        //creating a Referee and Match object
        Referee referee = null;
        Match match = null;

        //checking to see if the referee exists in the tournament
        for(Referee r : listReferees){
            if(r.getName().equalsIgnoreCase(refereeName)){
                referee = r;
            }
        }

        //checking to see if the match exists in the tournament
        for(Match m : listMatches){
            if(m.getDateTime().equals(dateTime)){
                match = m;
            }
        }

        //if both the referee and the match exist, add the referee to the match
        if(referee != null && match != null){
            if(referee.getCountry().getCountryName().equalsIgnoreCase(match.getTeamA().getTeam().getCountry().getCountryName()) ||
                    referee.getCountry().getCountryName().equalsIgnoreCase(match.getTeamB().getTeam().getCountry().getCountryName())){
                throw new IllegalArgumentException("Could not add referee to match. " +
                        "The referee cannot be from the same country as either team A or team B.");
            }
            match.addReferee(referee);
        }
        //throwing an error if the referee doesnt exist
        else if(referee == null){
            throw new IllegalArgumentException("The referee '" + refereeName + "' does not exist.");
        }
        //throwing an error if the match doesnt exist
        else if(match == null){
            throw new IllegalArgumentException("This match does not exist.");
        }
        //handling other errors
        else{
            throw new IllegalArgumentException("Error!");
        }
    }

    public void addPlayerToMatch(LocalDate dateTime, String teamName, String playerName)
            throws IllegalArgumentException{
        //creating Match, Team, and Player objects
        Match match = null;
        Team team = null;
        Player player = null;

        //checking to see if the match exists in the tournament
        for (Match m : listMatches){
            if(m.getDateTime().equals(dateTime)){
                match = m;
            }
        }

        //checking to see if the team exists in the tournament
        for(Team t : listTeams){
            if(t.getName().equalsIgnoreCase(teamName)){
                team = t;
            }
        }

        //if the team exists in the tournament, search for the player
        if(team != null){
            for (Player p : team.getSquad()){
                if(p.getName().equalsIgnoreCase(playerName)){
                    player = p;
                }
            }
        }

        //if the match, the team, and the player all exist in the tournament, add the player to the match
        if(match != null && team != null && player != null){
            match.addPlayer(player, team);
        }
        //throwing an error if the match doesnt exist
        else if(match == null){
            throw new IllegalArgumentException("This match does not exist.");
        }
        //throwing an error if the team doesnt exist
        else if(team == null){
            throw new IllegalArgumentException("The team '" + teamName + "' does not exist.");
        }
        //throwing an error if the player doesnt exist
        else if(player == null){
            throw new IllegalArgumentException("The player '" + playerName +
                    "' does not exist or is not part of this team.");
        }
        //handling other errors
        else{
            throw new IllegalArgumentException("Error!");
        }
    }

    public void setMatchScore(LocalDate dateTime, int score1, int score2) throws IllegalArgumentException{
        //creating a Match object
        Match match = null;
        LocalDate localDate = LocalDate.now(ZoneId.systemDefault());

        //check to see if the match exists in the tournament
        for(Match m : listMatches){
            if(m.getDateTime().equals(dateTime)){
                match = m;
            }
        }

        //if the match exists, set the score
        if(match != null){
            if(localDate.isBefore(dateTime)){
                throw new IllegalArgumentException("This match has not taken place yet.");
            }
            match.setMatchScore(score1, score2);
        }
        //throwing an error if the match doesnt exist
        else if(match == null){
            throw new IllegalArgumentException("This match does not exist.");
        }
        //handling other errors
        else{
            throw new IllegalArgumentException("Error!");
        }
    }

    public List<Match> getUpcomingMatches(){
        //creating a list to store the upcoming matches
        List<Match> upcomingMatches = new ArrayList<>();

        //adding upcoming matches to the list
        for (Match m : listMatches){
            if(m.isUpcoming() == true){
                upcomingMatches.add(m);
            }
        }
        return upcomingMatches;
    }

    public List<Match> getMatchesOn(LocalDate dateTime){
        //creating a new list to store the matches on the specified date
        List<Match> matchesOn = new ArrayList<>();

        //adding specified matches to the list
        for(Match m : listMatches){
            if(m.getDateTime().equals(dateTime)){
                matchesOn.add(m);
            }
        }
        return matchesOn;
    }

    public List<Match> getMatchesFor(String teamName){
        //creating a new list to store matches for a specific team
        List<Match> matchesFor = new ArrayList<>();

        //adding matches for specific team to the list
        for(Match m : listMatches){
            if(m.getTeamA().getTeam().getName().equalsIgnoreCase(teamName)){
                matchesFor.add(m);
            }
            else if(m.getTeamB().getTeam().getName().equalsIgnoreCase(teamName)){
                matchesFor.add(m);
            }
        }
        return matchesFor;
    }

    public List<LineUp> getMatchLineUps(LocalDate dateTime){
        //creating a new list to store match lineups for a specific date
        List<LineUp> matchLineUps = new ArrayList<>();

        //adding the lineups for the specified date
        for (Match m : listMatches){
            if (m.getDateTime().equals(dateTime)){
                matchLineUps.add(m.getTeamA());
                matchLineUps.add(m.getTeamB());
            }
        }
        return matchLineUps;
    }


    public synchronized String toString(){
        StringBuilder output = new StringBuilder();
        output.append(String.format("Tournament name: %s\n", this.name));
        output.append(String.format("Start Date: %s\n", this.startDate));
        output.append(String.format("End Date: %s\n", this.endDate));

        return output.toString();
    }

}
