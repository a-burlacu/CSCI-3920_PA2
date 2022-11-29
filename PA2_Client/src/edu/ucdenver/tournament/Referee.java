package edu.ucdenver.tournament;

public class Referee {
    private String name;

    private Country country;

    public Referee(String name, Country country){
        this.name = name;
        this.country = country;
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

    @Override
    public String toString(){
        return String.format("\nReferee Name: %s \nReferee Country: %s \n", this.name, this.country);
    }
}
