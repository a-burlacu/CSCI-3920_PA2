package edu.ucdenver.tournament;

import java.util.ArrayList;
import java.util.List;

public class Team {
    private String name;

    private Country country;
    private List<Player> squad;

    public Team(String name, Country country){
        this.name = name;
        this.country = country;
        this.squad = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Country getCountry(){
        return this.country;
    }

    public List<Player> getSquad(){
        return this.squad;
    }

    public void addPlayer(String name, int age, double height, double weight) throws IllegalArgumentException{
        Player player = new Player(name, age, height, weight);
        if(squad.size() > 35){
            throw new IllegalArgumentException("Player '" + name +
                    "' cannot be added because the max of 35 players has been reached");
        }
        this.squad.add(player);
    }

    @Override
    public String toString(){
        return String.format("\nTeam Name: %s \nTeam Country: %s \n", this.name, this.country);
    }


}
