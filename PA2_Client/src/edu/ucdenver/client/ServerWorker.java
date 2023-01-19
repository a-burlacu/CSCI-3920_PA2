//package edu.ucdenver.client;
//
//import edu.ucdenver.app.AdminApp;
//import edu.ucdenver.app.UserApp;
//import edu.ucdenver.tournament.Tournament;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.Socket;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//public class ServerWorker implements Runnable{
//
//    private final Socket clientConnection; // the reference is not changing, but the state(content of the object) will
//    private final String clientType;             // ADMIN or USER type
//    private final int id;                        // number of client
//    private Tournament tournament;
//    private PrintWriter output;
//    private BufferedReader input;
//    private boolean keepRunningClient;
//
//    ArrayList<AdminApp> adminApps = new ArrayList<>(); //List of admin connections
//    ArrayList<UserApp> userApps = new ArrayList<>(); //List of user connections
//    private int userCounter;
//    private int adminCounter;
//
//    //--------------------------------------------------
//    //                  constructors
//    //--------------------------------------------------
//
//    public ServerWorker(Socket connection, String clientType, int id){
//        this.clientConnection = connection;
//        this.clientType = clientType;
//        this.id = id;
//        this.keepRunningClient = true;
//        this.adminCounter = 0;
//        this.userCounter = 0;
//    }
//
//    //--------------------------------------------------
//    //                  send message
//    //--------------------------------------------------
//    private void sendMessage(String message) {
//        displayMessage("SERVER RECEIVED >> " + message);
//        this.output.println(message);
//    }
//
//    //--------------------------------------------------
//    //                  display message
//    //--------------------------------------------------
//    private void displayMessage(String message) {
//        System.out.printf("[%s %d] >> %s%n", this.clientType,this.id, message);
//    }
//
//    //--------------------------------------------------
//    //                   get output
//    //--------------------------------------------------
//    private void getOutputStream(Socket clientConnection) throws IOException {
//        this.output = new PrintWriter(clientConnection.getOutputStream(), true);
//    }
//
//    //--------------------------------------------------
//    //                   get input
//    //--------------------------------------------------
//    private void getInputStream(Socket clientConnection) throws IOException {
//        this.input = new BufferedReader(new InputStreamReader(clientConnection.getInputStream()));
//    }
//
//    //--------------------------------------------------
//    //               close connection
//    //--------------------------------------------------
//    private void closeClientConnection() {
//        // Try to close all input, output and socket
//        try {
//            this.input.close();
//        } catch (IOException | NullPointerException e) {
//            e.printStackTrace();
//        }
//        try {
//            this.output.close();
//        } catch (NullPointerException e) {
//            e.printStackTrace();
//        }
//        try {
//            this.clientConnection.close();
//        } catch (IOException | NullPointerException e) {
//            e.printStackTrace();
//        }
//    }
//
//    //--------------------------------------------------
//    //             process client request
//    //--------------------------------------------------
//    private void processClientCommand() throws IOException {
//
//        String clientMessage = this.input.readLine(); //recv from client
//        displayMessage("CLIENT COMMAND >> " + clientMessage);
//
//        /*  ::: PROTOCOL :::
//            determine if clientType = ADMIN or clientType = USER
//            specify which actions to execute depending on type
//            define functions and assign access
//         */
//
//        String[] arglist = clientMessage.split("\\|"); // this splits the string using | as delimiter
//        String response = ""; // This will be the response to the server
//
//        try {
//            switch (arglist[0]) {
//                case "0":       // TEST CASE
//                    System.out.println("selected case 0");
//                    response = "OK";
//                    break;
//                case "1":       // load from file
//                    tournament = Tournament.loadFromFile(arglist[1]);
//                    System.out.println("selected case 1");
//                    response = "OK";
//                    // tournament.getTeamName etc....
//                    break;
//
//                case "2":       // save to file
//                    tournament.saveToFile(arglist[1]);
//                    response = "OK";
//                    break;
//
//                case "3":       //create a new tournament
//                    String[] start = arglist[2].split("-"); //"YYYY"-"MM-"DD"
//                    LocalDate startDate = LocalDate.of(Integer.parseInt(start[0]), Integer.parseInt(start[1]), Integer.parseInt(start[2]));
//
//                    String[] end = arglist[3].split("-"); //"YYYY"-"MM-"DD"
//                    LocalDate endDate = LocalDate.of(Integer.parseInt(end[0]), Integer.parseInt(end[1]), Integer.parseInt(end[2]));
//
//                    // not sure if this should be 'this.tournament' or just 'tournament' since it was never initialized in constructor
//                    tournament = new Tournament(arglist[1], startDate, endDate);
//                    response = "OK";
//                    break;
//
//                case "4":       //add country
//                    tournament.addCountry(arglist[1].toString());
//                    response = "OK";
//                    break;
//
//                case "5":       //add a national team representing a country
//                    tournament.addTeam(arglist[1], arglist[2]);
//                    response = "OK";
//                    break;
//
//                case "6":       //add referee
//                    tournament.addReferee(arglist[1], arglist[2]);
//                    response = "OK";
//                    break;
//
//                case "7":       // add a player to a national team squad
//                    tournament.addPlayer(arglist[1], arglist[2], Integer.parseInt(arglist[3]),
//                            Double.parseDouble(arglist[4]), Double.parseDouble(arglist[5]));
//                    response = "OK";
//                    break;
//
//                case "8":       //add a match on a particular date between two national teams
//                    String[] matchDate = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateMatch = LocalDate.of(Integer.parseInt(matchDate[0]), Integer.parseInt(matchDate[1]), Integer.parseInt(matchDate[2]));
//                    tournament.addMatch(dateMatch, arglist[2], arglist[3]);
//                    response = "OK";
//                    break;
//
//                case "9":       //assign a referee to a match
//                    String[] refDate = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateRef = LocalDate.of(Integer.parseInt(refDate[0]), Integer.parseInt(refDate[1]), Integer.parseInt(refDate[2]));
//                    tournament.addRefereeToMatch(dateRef, arglist[2]);
//                    response = "OK";
//                    break;
//
//                case "10":      //add a player to a national teams lineup for a particular match
//                    String[] lineupDate = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateLineup = LocalDate.of(Integer.parseInt(lineupDate[0]), Integer.parseInt(lineupDate[1]), Integer.parseInt(lineupDate[2]));
//                    tournament.addPlayerToMatch(dateLineup, arglist[2], arglist[3]);
//                    response = "OK";
//                    break;
//
//                case "11":      //record the score of a completed match
//                    String[] scoreDate = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateScore = LocalDate.of(Integer.parseInt(scoreDate[0]), Integer.parseInt(scoreDate[1]), Integer.parseInt(scoreDate[2]));
//                    tournament.setMatchScore(dateScore, Integer.parseInt(arglist[2]), Integer.parseInt(arglist[3]));
//                    response = "OK";
//                    break;
//
//                case "12":        //get a list of the upcoming matches
//                    response = tournament.getUpcomingMatches().toString();
//                    break;
//
//                case "13":        //get a list of matches on a particular date
//                    String[] matchDateU = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateMatchU = LocalDate.of(Integer.parseInt(matchDateU[0]), Integer.parseInt(matchDateU[1]), Integer.parseInt(matchDateU[2]));
//                    response = tournament.getMatchesOn(dateMatchU).toString();
//                    break;
//
//
//                case "14":        // get a list of all games for a specific team
//                    response = tournament.getMatchesFor(arglist[1]).toString();
//                    break;
//
//                case "15":        //get the lineups for a match either past or future
//                    String[] lineupDateU = arglist[1].split("-"); //"YYYY","MM,"DD"
//                    LocalDate dateLineupU = LocalDate.of(Integer.parseInt(lineupDateU[0]), Integer.parseInt(lineupDateU[1]), Integer.parseInt(lineupDateU[2]));
//                    response = tournament.getMatchLineUps(dateLineupU).toString();
//                    break;
//            }
//
//        }catch (IllegalArgumentException iae) {
//            response = "ERR| " + iae.getMessage();
//        }
//        this.sendMessage(response);
//
//    }
//
//    //--------------------------------------------------
//    //               shutdown server
//    //--------------------------------------------------
//    private void forceShutdown() throws IOException {
//
//        this.keepRunningClient = false;
//
//        clientConnection.close();
//    }
//
//    @Override
//    public void run() {
//        System.out.println("<status message: running ClientWorker...>");
//        BufferedReader input;
//        PrintWriter output;
//        String newMessage;
//
//
//        displayMessage("Getting data streams...");
//        try {
//            getOutputStream(clientConnection);
//            getInputStream(clientConnection);
//
//            // Now we process the requests and send the responses
//            displayMessage("Connected to simple Java Server");
//
//
//            while (this.keepRunningClient) {
//
//                processClientCommand();
//            }
//        }
//        catch (IOException e) {
//            e.printStackTrace();
//        }
//        finally{
//            closeClientConnection();
//        }
//    }
//}
