package edu.ucdenver.client;

import edu.ucdenver.tournament.Tournament;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDate;

public class Client {
    private final int serverPort;
    private final String serverIP;
    private boolean isConnected;
    private Socket clientSocket;
    private PrintWriter output;
    private BufferedReader input;
    private final String filename;

    Tournament tournament;

    public Client(String ip, int port) {
        this.serverPort = port;
        this.serverIP = ip;
        this.isConnected = false;
        this.tournament = null;
        this.filename = "tournament.ser";
    }

    public Client() {
        this("localhost", 9888); // Default constructor, use the predefined ip/port we are using
    }

    public boolean isConnected() {
        return this.isConnected;
    }


    private PrintWriter getOutputStream() throws IOException {
        return new PrintWriter(this.clientSocket.getOutputStream(), true);
    }

    private BufferedReader getInputStream() throws IOException {
        return new BufferedReader(new InputStreamReader(this.clientSocket.getInputStream()));
    }


    public void connect() {
        System.out.println("<status message: new client created>");
        displayMessage("Attempting connection to server....");
        try {
            this.clientSocket = new Socket(this.serverIP, this.serverPort);
            this.isConnected = true; // At this point, if no exception then we're connected!
            this.output = this.getOutputStream();
            this.input = this.getInputStream();
            displayMessage("Connected to server");

        } catch (IOException e) { // If something went wrong...
            this.input = null;
            this.output = null;
            this.clientSocket = null;
            this.isConnected = false;
            displayMessage("Not Connected to server");
            // Something went wrong, and we don't know the state, so re-initialize everything
            // This will make the client consistent!!
        }
    }

    public void getServerInitialResponse() throws IOException {
        String srvResponse = this.input.readLine();
        displayMessage("SERVER >> " + srvResponse);

    }

    public void disconnect() {
        displayMessage(">> Terminating Client Connection to Server");
        try {
            this.input.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        try {
            this.output.close();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            this.clientSocket.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        displayMessage("Disconnected");
    }

    public void getServerRequest(String message){
        //process message from Server to execute commands
        String cmd = "";
        switch (message) {
            case "LOAD":
                //tournament = Tournament.loadFromFile(filename);
                cmd = String.format("%s|%s", "1", filename);
                try {
                    displayMessage(executeCommand(cmd));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            case "SAVE":
                //tournament.saveToFile(filename);
                cmd = String.format("%s|%s", "2", filename);
                try {
                    displayMessage(executeCommand(cmd));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            case "STOP":
                disconnect();
        }
    }
    // send internal request -> Tournament stuff
    public String executeCommand(String command) throws IOException { // Send message and returns the server response

        String[] arglist = command.split("\\|"); // this splits the string using | as delimiter
        String response = ""; // This will be the response to the server

        try {
            switch (arglist[0]) {
                case "0":       // TEST CASE
                    System.out.println("selected case 0");
                    response = "OK";
                    break;
                case "1":       // load from file
                    tournament = Tournament.loadFromFile(arglist[1]);
                    System.out.println("selected case 1");
                    response = "OK";
                    // tournament.getTeamName etc....
                    break;

                case "2":       // save to file
                    tournament.saveToFile(arglist[1]);
                    response = "OK";
                    break;

                case "3":       //create a new tournament
                    String[] start = arglist[2].split("-"); //"YYYY"-"MM-"DD"
                    LocalDate startDate = LocalDate.of(Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(start[2]));

                    String[] end = arglist[3].split("-"); //"YYYY"-"MM-"DD"
                    LocalDate endDate = LocalDate.of(Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));

                    // not sure if this should be 'this.tournament' or just 'tournament' since it was never initialized in constructor
                    tournament = new Tournament(arglist[1], startDate, endDate);
                    response = "OK";
                    break;

                case "4":       //add country
                    tournament.addCountry(arglist[1].toString());
                    response = "OK";
                    break;

                case "5":       //add a national team representing a country
                    tournament.addTeam(arglist[1], arglist[2]);
                    response = "OK";
                    break;

                case "6":       //add referee
                    tournament.addReferee(arglist[1], arglist[2]);
                    response = "OK";
                    break;

                case "7":       // add a player to a national team squad
                    tournament.addPlayer(arglist[1], arglist[2], Integer.parseInt(arglist[3]),
                            Double.parseDouble(arglist[4]), Double.parseDouble(arglist[5]));
                    response = "OK";
                    break;

                case "8":       //add a match on a particular date between two national teams
                    String[] matchDate = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateMatch = LocalDate.of(Integer.parseInt(matchDate[0]), Integer.parseInt(matchDate[1]), Integer.parseInt(matchDate[2]));
                    tournament.addMatch(dateMatch, arglist[2], arglist[3]);
                    response = "OK";
                    break;

                case "9":       //assign a referee to a match
                    String[] refDate = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateRef = LocalDate.of(Integer.parseInt(refDate[0]), Integer.parseInt(refDate[1]), Integer.parseInt(refDate[2]));
                    tournament.addRefereeToMatch(dateRef, arglist[2]);
                    response = "OK";
                    break;

                case "10":      //add a player to a national teams lineup for a particular match
                    String[] lineupDate = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateLineup = LocalDate.of(Integer.parseInt(lineupDate[0]), Integer.parseInt(lineupDate[1]), Integer.parseInt(lineupDate[2]));
                    tournament.addPlayerToMatch(dateLineup, arglist[2], arglist[3]);
                    response = "OK";
                    break;

                case "11":      //record the score of a completed match
                    String[] scoreDate = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateScore = LocalDate.of(Integer.parseInt(scoreDate[0]), Integer.parseInt(scoreDate[1]), Integer.parseInt(scoreDate[2]));
                    tournament.setMatchScore(dateScore, Integer.parseInt(arglist[2]), Integer.parseInt(arglist[3]));
                    response = "OK";
                    break;

                case "12":        //get a list of the upcoming matches
                    response = tournament.getUpcomingMatches().toString();
                    break;

                case "13":        //get a list of matches on a particular date
                    String[] matchDateU = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateMatchU = LocalDate.of(Integer.parseInt(matchDateU[0]), Integer.parseInt(matchDateU[1]), Integer.parseInt(matchDateU[2]));
                    response = tournament.getMatchesOn(dateMatchU).toString();
                    break;


                case "14":        // get a list of all games for a specific team
                    response = tournament.getMatchesFor(arglist[1]).toString();
                    break;

                case "15":        //get the lineups for a match either past or future
                    String[] lineupDateU = arglist[1].split("-"); //"YYYY","MM,"DD"
                    LocalDate dateLineupU = LocalDate.of(Integer.parseInt(lineupDateU[0]), Integer.parseInt(lineupDateU[1]), Integer.parseInt(lineupDateU[2]));
                    response = tournament.getMatchLineUps(dateLineupU).toString();
                    break;
                case "16":
                    disconnect();
            }

        }catch (IllegalArgumentException iae) {
            response = "ERR| " + iae.getMessage();
        }
        return response;

        //        String srvResponse = "";
//        String[] requestList = request.split("\\|"); // this splits the string using | as delimiter
//        if(requestList[0].equals("16")){
//            this.disconnect();
//        }
//        else {
//
//            this.output.println(request);
//            displayMessage("CLIENT REQUEST >> " + request);
//            srvResponse = this.input.readLine();
//            displayMessage("SERVER RESPONSE << " + srvResponse);
//        }
//            return srvResponse;
    }

    private void displayMessage(String message) { // We can improve this method to be log-type one
        System.out.println("[CLIENT] " + message);
    }
}

