package edu.ucdenver.tournament;

public class Player {
    private String name;
    private int age;
    private double height;
    private double weight;

    public Player(String name, int age, double height, double weight) {
        this.name = name;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getHeight() {
        return this.height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString(){
        return String.format("\nPlayer Name: %s \nPlayer Age: %d \nPlayer Height: %f \nPlayer Weight: %f \n",
                this.name, this.age, this.height, this.weight);
    }
}
