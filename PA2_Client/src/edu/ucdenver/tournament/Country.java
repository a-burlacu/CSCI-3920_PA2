package edu.ucdenver.tournament;

public class Country {
    private String countryName;

    public Country(String countryName){
        this.countryName = countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryName(){
        return this.countryName;
    }

    @Override
    public String toString(){
        return String.format(this.countryName);
    }

}
