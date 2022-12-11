package edu.ucdenver.client;

import edu.ucdenver.tournament.Tournament;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final int serverPort;
    private final String serverIP;
    private boolean isConnected;
    private Socket serverConnection;
    private PrintWriter output;
    private BufferedReader input;
    private String filename;

    Tournament tournament;

    public Client(String ip, int port) {
        this.serverPort = port;
        this.serverIP = ip;
        this.isConnected = false;
        this.tournament = null;
        this.filename = "tournament.ser";
    }

    public Client() {
        this("127.0.0.1", 9888); // Default constructor, use the predefined ip/port we are using
    }

    public boolean isConnected() {
        return this.isConnected;
    }


    private PrintWriter getOutputStream() throws IOException {
        return new PrintWriter(this.serverConnection.getOutputStream(), true);
    }

    private BufferedReader getInputStream() throws IOException {
        return new BufferedReader(new InputStreamReader(this.serverConnection.getInputStream()));
    }


    public void connect() {
        System.out.println("<status message: new client created>");
        displayMessage("Attempting connection to server....");
        try {
            this.serverConnection = new Socket(this.serverIP, this.serverPort);
            this.isConnected = true; // At this point, if no exception then we're connected!
            this.output = this.getOutputStream();
            this.input = this.getInputStream();
            displayMessage("Connected to server");

        } catch (IOException e) { // If something went wrong...
            this.input = null;
            this.output = null;
            this.serverConnection = null;
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
            this.serverConnection.close();
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
        }
        displayMessage("Disconnected");
    }

    public void getServerRequest(String message){
        //process message from Server to execute commands
        switch (message) {
            case "LOAD":
                tournament = Tournament.loadFromFile(filename);
            case "SAVE":
                tournament.saveToFile(filename);
            case "STOP":
                disconnect();
        }
    }

    private void displayMessage(String message) { // We can improve this method to be log-type one
        System.out.println("[CLIENT] " + message);
    }
}

