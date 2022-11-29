package edu.ucdenver.tournament;

import java.util.ArrayList;
import java.util.List;

public class LineUp {

    private Team team;
    private List<Player> listOfPlayers;

    public LineUp(Team team){
        this.team = team;
        this.listOfPlayers = new ArrayList<>();
    }

    public Team getTeam(){
        return this.team;
    }

    public List<Player> getPlayers(){
        return this.listOfPlayers;
    }

    public void addPlayer(Player player) throws IllegalArgumentException{
        if(this.listOfPlayers.size() > 11){
            throw new IllegalArgumentException("Player '" + player.getName() +
                    "' could not be added. The max of 11 players per lineup has been reached.");
        }
        this.listOfPlayers.add(player);
    }

    @Override
    public String toString(){
        return String.format("\nTeam: %s \nList of Players: %s \n", this.team, this.listOfPlayers);
    }
}
