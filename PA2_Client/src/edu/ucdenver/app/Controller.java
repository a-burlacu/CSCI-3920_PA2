package edu.ucdenver.app;

import edu.ucdenver.server.Client;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Controller {
    //---------------------------------------
    //             Admin GUI
    //---------------------------------------
    public TextField txtNewTournamentName;

    public Button btnNewTournamentSubmit;
    public DatePicker dtpNewTournamentStartDate;
    public DatePicker dtpNewTournamentEndDate;
    public TextField txtAddCountry;

    public Button btnAddCountrySubmit;
    public Tab tabAddTeam;
    public TextField txtAddTeamName;
    public Button btnAddTeamSubmit;
    public ComboBox selAddTeamCountry;
    public Tab tabAddReferee;
    public TextField txtAddRefereeName;
    public Button btnAddRefereeSubmit;
    public ComboBox selAddRefereeCountry;
    public Tab tabAddPlayerToSquad;
    public Button btnAddPlayerToSquadSubmit;
    public TextField txtAddPlayerToSquadName;
    public TextField txtAddPlayerToSquadAge;
    public TextField txtAddPlayerToSquadHeight;
    public TextField txtAddPlayerToSquadWeight;
    public ComboBox selAddPlayerToSquadTeam;
    public Tab tabAddMatch;
    public Button btnAddMatchSubmit;
    public ComboBox selAddMatchTeamA;
    public ComboBox selAddMatchTeamB;
    public DatePicker dtpAddMatchDate;
    public Tab tabAddRefereeToMatch;
    public Button btnAddRefereeToMatchSubmit;
    public ComboBox selAddRefereeToMatchDate;
    public ComboBox selAddRefereeName;
    public Tab tabAddPlayerToLineUp;
    public Button btnAddPlayerToLineUpSubmit;
    public ComboBox selAddPlayerToLineupMatch;
    public ComboBox selAddPlayerToLineUpTeam;
    public ComboBox selAddPlayerToLineUpPlayer;
    public Tab tabRecordAMatchScore;
    public Button btnSetMatchScoreSubmit;
    public TextField txtSetMatchScore1;
    public TextField txtSetMatchScore2;
    public ComboBox selSetMatchScoreDate;
    public Button btnLoadFromFile;
    public Button btnSaveToFile;
    public Button btnExitAdminApp;

    //---------------------------------------
    //              User GUI
    //---------------------------------------
    public ListView lstUpcomingMatches;
    public Tab tabUpcomingMatches;
    public Tab tabMatchesByDate;
    public ListView lstMatchesByDate;
    public Button btnMatchByDateSubmit;
    public ComboBox selMatchByDate;
    public Tab tabMatchesByTeam;
    public ListView lstMatchesByTeam;
    public Button btnMatchesByTeamSubmit;
    public ComboBox selMatchesByTeam;
    public Tab tabLineUpsByMatch;
    public ListView lstLineUpsByMatch;
    public Button btnLineUpsByMatchSubmit;
    public ComboBox selLineUpsByMatch;
    public Button btnExitUserApp;
    Client client;


    private List<String> countries;
    private List<String> teams;
    private List<String> referees;
    private List<LocalDate> matches;
    private List<String> players;


    public Controller() {
        client = new Client();
        client.connect();
        this.countries = new ArrayList<>();
        this.teams = new ArrayList<>();
        this.referees = new ArrayList<>();
        this.matches = new ArrayList<>();
        this.players = new ArrayList<>();
        this.selAddTeamCountry = new ComboBox();
        this.selAddRefereeCountry = new ComboBox();
        this.selAddPlayerToSquadTeam = new ComboBox();
        this.selAddMatchTeamA = new ComboBox();
        this.selAddMatchTeamB = new ComboBox();
        this.selAddRefereeToMatchDate = new ComboBox();
        this.selAddRefereeName = new ComboBox<>();
        this.selAddPlayerToLineupMatch = new ComboBox();
        this.selAddPlayerToLineUpTeam = new ComboBox<>();
        this.selAddPlayerToLineUpPlayer = new ComboBox();
        this.selSetMatchScoreDate = new ComboBox<>();
        this.lstUpcomingMatches = new ListView<>();
        this.lstMatchesByDate = new ListView<>();
        this.selMatchByDate = new ComboBox<>();
        this.lstMatchesByTeam = new ListView<>();
        this.selMatchesByTeam = new ComboBox<>();
        this.lstLineUpsByMatch = new ListView<>();
        this.selLineUpsByMatch = new ComboBox<>();
    }

    private String sendCommand(String cmd) {
        String response;
        if (client.isConnected()) {
            try {
                response = client.sendRequest(cmd);

            } catch (IOException e) {
                response = "ERR|" + e.getMessage();
                //client.disconnect();
            }
        } else {
            response = "ERR| Client is not connected.";
        }
        return response;
    }

    //----------------------------TESTING------------------------------------
    public TextArea txtOutput;
    public TextField txtTestCmd;
    public Button btnTestSendCmd;
    //-----------------------------------------------------------------------
    //----------------------------TESTING------------------------------------
    public void testSendCmd(ActionEvent actionEvent) {
        String cmd = txtTestCmd.getText();
        Alert alert;

        if(client.isConnected()) {
            try {
                String response = client.sendRequest(cmd);
                String[] respArgs = response.split("\\|");

                switch (respArgs[0]){
                    case "OK":
                        alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                        alert.show();
                        break;
                    case "ERR":
                        alert = new Alert(Alert.AlertType.ERROR, "Error occurred", ButtonType.OK);
                        alert.show();
                        break;
                }
                txtOutput.clear();
                for (String s: respArgs){
                    txtOutput.appendText(s);
                    txtOutput.appendText(System.getProperty("line.separator"));
                }
            } catch (IOException e) {
                alert = new Alert(Alert.AlertType.ERROR, "Exception:" + e.getMessage(), ButtonType.OK);
                alert.show();
            }
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR, "Client is not connected.", ButtonType.OK);
            alert.show();
        }
    }
    //-----------------------------------------------------------------------



    //---------------------------------------
    //             Admin GUI
    //---------------------------------------

    public void createTournament(ActionEvent actionEvent) {
        String tournamentNameText = this.txtNewTournamentName.getText();
        LocalDate tournamentStartDate = this.dtpNewTournamentStartDate.getValue();
        LocalDate tournamentEndDate = this.dtpNewTournamentEndDate.getValue();
        Alert alert;

        String cmd = String.format("%s|%s|%s|%s","3",tournamentNameText, tournamentStartDate.toString(), tournamentEndDate.toString());
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }

    public void addCountry(ActionEvent actionEvent) {
        String countryNameText = this.txtAddCountry.getText();
        Alert alert;

        String cmd = String.format("%s|%s","4",countryNameText);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                countries.add(countryNameText);
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateCountrySelector(Event event) {
        if(this.tabAddTeam.isSelected()){
            this.selAddTeamCountry.setItems(FXCollections.observableArrayList(this.countries));
        }
    }

    public void addTeam(ActionEvent actionEvent) {
        this.selAddTeamCountry.setItems(FXCollections.observableArrayList(this.countries));
        String countryNameSelection = this.selAddTeamCountry.getValue().toString();
        String teamNameText = this.txtAddTeamName.getText();
        Alert alert;

        String cmd = String.format("%s|%s|%s","5", teamNameText, countryNameSelection);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                this.teams.add(teamNameText);
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateAddRefereeCountrySelector(Event event) {
        if(this.tabAddReferee.isSelected()){
            this.selAddRefereeCountry.setItems(FXCollections.observableArrayList(this.countries));
        }
    }

    public void AddReferee(ActionEvent actionEvent) {
        this.selAddRefereeCountry.setItems(FXCollections.observableArrayList(this.countries));
        String countryNameSelection = this.selAddRefereeCountry.getValue().toString();
        String refereeNameText = this.txtAddRefereeName.getText();
        Alert alert;

        String cmd = String.format("%s|%s|%s","6", refereeNameText, countryNameSelection);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                this.referees.add(refereeNameText);
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateAddPlayerToSquadTeamSelector(Event event) {
        if(this.tabAddPlayerToSquad.isSelected()){
            this.selAddPlayerToSquadTeam.setItems(FXCollections.observableArrayList(this.teams));
        }
    }
    public void addPlayerToSquad(ActionEvent actionEvent) {
        this.selAddPlayerToSquadTeam.setItems(FXCollections.observableArrayList(this.teams));
        String playerTeamSelection = this.selAddPlayerToSquadTeam.getValue().toString();
        String playerNameText = this.txtAddPlayerToSquadName.getText();
        String playerAgeText = this.txtAddPlayerToSquadAge.getText();
        String playerHeightText = this.txtAddPlayerToSquadHeight.getText();
        String playerWeightText = this.txtAddPlayerToSquadWeight.getText();
        Alert alert;

        String cmd = String.format("%s|%s|%s|%s|%s|%s","7", playerTeamSelection, playerNameText, playerAgeText,
                playerHeightText, playerWeightText);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                this.players.add(playerNameText);
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateAddMatchSelectors(Event event) {
        if(this.tabAddMatch.isSelected()){
            this.selAddMatchTeamA.setItems(FXCollections.observableArrayList(this.teams));
            this.selAddMatchTeamB.setItems(FXCollections.observableArrayList(this.teams));
        }
    }
    public void addMatch(ActionEvent actionEvent) {
        this.selAddMatchTeamA.setItems(FXCollections.observableArrayList(this.teams));
        this.selAddMatchTeamB.setItems(FXCollections.observableArrayList(this.teams));
        LocalDate dateOfMatch = dtpAddMatchDate.getValue();
        String teamASelection = selAddMatchTeamA.getValue().toString();
        String teamBSelection = selAddMatchTeamB.getValue().toString();
        Alert alert;

        String cmd = String.format("%s|%s|%s|%s","8", dateOfMatch.toString(), teamASelection, teamBSelection);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                this.matches.add(dateOfMatch);
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateAddRefereeToMatchSelectors(Event event) {
        if(this.tabAddRefereeToMatch.isSelected()){
            this.selAddRefereeToMatchDate.setItems(FXCollections.observableArrayList(this.matches));
            this.selAddRefereeName.setItems(FXCollections.observableArrayList(this.referees));
        }
    }
    public void addReferee(ActionEvent actionEvent) {
        this.selAddRefereeToMatchDate.setItems(FXCollections.observableArrayList(this.matches));
        this.selAddRefereeName.setItems(FXCollections.observableArrayList(this.referees));
        String dateOfMatch = selAddRefereeToMatchDate.getValue().toString();
        String refereeName = selAddRefereeName.getValue().toString();
        Alert alert;

        String cmd = String.format("%s|%s|%s","9", dateOfMatch, refereeName);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateAddPlayerToLineUpSelectors(Event event) {
        if(this.tabAddPlayerToLineUp.isSelected()){
            this.selAddPlayerToLineupMatch.setItems(FXCollections.observableArrayList(this.matches));
            this.selAddPlayerToLineUpTeam.setItems(FXCollections.observableArrayList(this.teams));
            this.selAddPlayerToLineUpPlayer.setItems(FXCollections.observableArrayList(this.players));
        }
    }
    public void addPlayerToLineUp(ActionEvent actionEvent) {
        this.selAddPlayerToLineupMatch.setItems(FXCollections.observableArrayList(this.matches));
        this.selAddPlayerToLineUpTeam.setItems(FXCollections.observableArrayList(this.teams));
        this.selAddPlayerToLineUpPlayer.setItems(FXCollections.observableArrayList(this.players));
        String matchDate = selAddPlayerToLineupMatch.getValue().toString();
        String teamName = selAddPlayerToLineUpTeam.getValue().toString();
        String playerName = selAddPlayerToLineUpPlayer.getValue().toString();
        Alert alert;

        String cmd = String.format("%s|%s|%s|%s","10", matchDate, teamName, playerName);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void updateRecordAMatchScoreSelectors(Event event) {
        if(this.tabRecordAMatchScore.isSelected()){
            this.selSetMatchScoreDate.setItems(FXCollections.observableArrayList(this.matches));
        }
    }
    public void setMatchScore(ActionEvent actionEvent) {
        this.selSetMatchScoreDate.setItems(FXCollections.observableArrayList(this.matches));
        String matchDate = selSetMatchScoreDate.getValue().toString();
        String score1 = txtSetMatchScore1.getText();
        String score2 = txtSetMatchScore2.getText();
        Alert alert;

        String cmd = String.format("%s|%s|%s|%s","11", matchDate, score1, score2);
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void loadFromFile(ActionEvent actionEvent) {
        this.countries.clear();
        this.matches.clear();
        this.players.clear();
        this.referees.clear();

        String cmd = String.format("%s|%s", "1", "tournament.ser");
        String response = sendCommand(cmd);
        String [] responseArgs = response.split("\\|");

        String [] createTournament = responseArgs[1].split(",");
        String [] listOfCountries = responseArgs[2].split(",");
        String [] listOfMatches = responseArgs[3].split(",");
        String [] listOfTeams = responseArgs[4].split(",");
        String [] listOfReferees = responseArgs[5].split(",");
        String [] listOfPlayers = responseArgs[6].split(",");

        this.countries.addAll(Arrays.asList(listOfCountries));

        for(String s : listOfMatches){
            String[] date = s.split("-");
            LocalDate localDate = LocalDate.of(Integer.parseInt(date[0]), Integer.parseInt(date[1]), Integer.parseInt(date[2]));
            this.matches.add(localDate);
        }

        this.teams.addAll(Arrays.asList(listOfTeams));

        this.referees.addAll(Arrays.asList(listOfReferees));

        this.players.addAll(Arrays.asList(listOfPlayers));

        cmd = String.format("%s|%s|%s|%s","3", createTournament[1], createTournament[2], createTournament[3]);
        response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");
        Alert alert;

        switch (respArgs[0]){
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void saveToFile(ActionEvent actionEvent) {
        String cmd = String.format("%s|%s", "1", "tournament.ser");
        String response = sendCommand(cmd);
        String[] respArgs = response.split("\\|");
        Alert alert;

        switch (respArgs[0]) {
            case "OK":
                alert = new Alert(Alert.AlertType.CONFIRMATION, "Server Response:" + response, ButtonType.OK);
                alert.show();
                break;
            case "ERR":
                alert = new Alert(Alert.AlertType.ERROR, respArgs[1], ButtonType.OK);
                alert.show();
                break;
        }
    }
    public void exitAdminApp(ActionEvent actionEvent) {
        Stage stage = (Stage) this.btnExitAdminApp.getScene().getWindow();
        stage.close();
        //client.disconnect();
    }

    //---------------------------------------
    //              User GUI
    //---------------------------------------

    public void listUpcomingMatches(Event event) {
        if(this.tabUpcomingMatches.isSelected()){
            String cmd = String.format("%s|","12");
            String response = sendCommand(cmd);
            String [] responseArgs = response.split(",");
            List<String> upcomingMatches = new ArrayList<>();

            for (String s : responseArgs){
                upcomingMatches.add(s);
            }
            this.lstUpcomingMatches.setItems(FXCollections.observableArrayList(upcomingMatches));
        }
    }
    public void updateMatchesByDateSelectors(Event event) {
        if(this.tabMatchesByDate.isSelected()){
            this.selMatchByDate.setItems(FXCollections.observableArrayList(this.matches));
        }
    }

    public void listMatchesByDate(ActionEvent actionEvent) {
        this.selMatchByDate.setItems(FXCollections.observableArrayList(this.matches));
        String matchDate = selMatchByDate.getValue().toString();
        String cmd = String.format("%s|%s","13", matchDate);
        String response = sendCommand(cmd);
        String [] responseArgs = response.split(",");
        List<String> test = new ArrayList<>();

        for (String s : responseArgs) {
            test.add(s);
        }
        this.lstMatchesByDate.setItems(FXCollections.observableArrayList(test));
    }
    public void updateMatchesByTeamSelectors(Event event) {
        if(this.tabMatchesByTeam.isSelected()){
            this.selMatchesByTeam.setItems(FXCollections.observableArrayList(this.teams));
        }
    }
    public void listMatchesByTeam(ActionEvent actionEvent) {
        this.selMatchesByTeam.setItems(FXCollections.observableArrayList(this.teams));
        String matchTeam = selMatchesByTeam.getValue().toString();
        String cmd = String.format("%s|%s","14", matchTeam);
        String response = sendCommand(cmd);
        String [] responseArgs = response.split(",");
        List<String> test = new ArrayList<>();

        for(String s : responseArgs) {
            test.add(s);
        }

        this.lstMatchesByTeam.setItems(FXCollections.observableArrayList(test));
    }
    public void updateLineUpsByMatchSelectors(Event event) {
        if(this.tabLineUpsByMatch.isSelected()){
            this.selLineUpsByMatch.setItems(FXCollections.observableArrayList(this.matches));
        }
    }
    public void listLineUpsByMatch(ActionEvent actionEvent) {
        this.selLineUpsByMatch.setItems(FXCollections.observableArrayList(this.matches));
        String matchDate = selLineUpsByMatch.getValue().toString();
        String cmd = String.format("%s|%s", "15", matchDate);
        String response = sendCommand(cmd);
        String [] responseArgs = response.split(",");
        List<String> test = new ArrayList<>();

        for(String s : responseArgs){
            test.add(s);
        }

        this.lstLineUpsByMatch.setItems(FXCollections.observableArrayList(test));
    }

    public void exitUserApp(ActionEvent actionEvent) {
        Stage stage = (Stage) this.btnExitUserApp.getScene().getWindow();
        stage.close();
        //client.disconnect();
    }
}