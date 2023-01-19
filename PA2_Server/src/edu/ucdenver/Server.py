from edu.ucdenver.Tournament import Tournament
from edu.ucdenver.ClientWorker import ClientWorker
from edu.ucdenver.Tournament.Team import Team
from socket import socket, AF_INET, SOCK_STREAM
from datetime import datetime, time
import time


class Server:

    def __init__(self, port, backlog):
        # Constructor
        self.port = port
        self.backlog = backlog
        self.tournament = Tournament.Tournament("", "", "")
        self.connection_counter = 0
        self.server_socket = socket(AF_INET, SOCK_STREAM)
        # self.client_socket = client_socket
        print("socket created")
        self.cw = ClientWorker("", "", "")

    # Run server function
    def run_server(self):
        server_socket = socket(AF_INET, SOCK_STREAM)  # create a socket object
        server_socket.bind(('localhost', 10003))  # bind to the port
        server_socket.listen(5)  # queue up to 5 requests
        connected = True
        client_socket, addr = server_socket.accept()  # Wait for a client connection
        while connected:
            print("Got a connection from {}".format(str(addr)))  # Print the client address
            msg = ".\r\n"
            client_socket.send(msg.encode('ascii'))
            message = client_socket.recv(1024)
            message = message.decode()
            msg2 = self.process_client_request(message)
            client_socket.send(msg2.encode('ascii'))
        client_socket.close()
        print("hi")

    # Processes all client requests
    def process_client_request(self, client_msg):
        tokens = client_msg.split(',')
        print(client_msg)
        for t in tokens:
            print(t)

        # Create Tournament
        # Creates a tournament based on information sent from the
        # client message
        if client_msg.startswith("CREATE,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            tokens[3] = tokens[3].replace("\r\n", "")
            first_date = tokens[2].split('-')
            second_date = tokens[3].split('-')
            start_date = datetime(int(first_date[0]), int(first_date[1]), int(first_date[2]))
            end_date = datetime(int(second_date[0]), int(second_date[1]), int(second_date[2]))
            start_date.strftime("%Y-%m-%d")
            end_date.strftime("%Y-%m-%d")
            self.tournament.name = tokens[1]
            self.tournament.start_date = start_date
            self.tournament.end_date = end_date
            print(self.tournament.__str__())
            this_message = "Successfully Created Tournament"
            return this_message
        ###################################################################################################
        if client_msg.startswith("LOAD,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message
        ###################################################################################################
        if client_msg.startswith("SAVE,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            filename = tokens[1]
            self.tournament.save_file(filename)
            this_message = "SAVED TO FILE!!!!!!!!!!"
            return this_message
        ###################################################################################################
        # Adds a Country
        # Adds a country to the tournament based on
        # the country name sent in with the clientMessage
        if client_msg.startswith("ADDCOUNTRY,"):
            # parse clientmessage into array
            # add to tournament
            tokens[1] = tokens[1].replace("\r\n", "")
            country_name = tokens[1]
            already_there = False
            for c in self.tournament.countries:
                if c.name == country_name:
                    already_there = True
                    this_message = "[ERROR] - Country Is Already In System"
                    return this_message
            if not already_there:
                self.tournament.add_country(tokens[1])
                this_message = "Successfully Added Country"
                return this_message

        # Adds a Team
        # Adds a team to the tournament based on
        # the team name and team country sent in with the clientMessage
        if client_msg.startswith("ADDTEAM,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            country_found = False
            same_name = False
            msg = ""
            for c in self.tournament.countries:
                if c.name.lower() == tokens[2].lower():
                    country_found = True
                    for t in self.tournament.teams:
                        if t.name.lower() == tokens[1].lower():
                            same_name = True
                            msg = "[ERROR] - Team With Name: " + tokens[1] + " Already Exists"
                            return msg
                    if country_found is True and same_name is False:
                        self.tournament.add_team(tokens[1], c)
                        msg = "Successfully Added Team"
                        return msg
            if not country_found:
                msg = "[ERROR] - Must Add Country Before Adding Team"
                return msg

        # Adds a Referee
        # Adds a referee to the tournament based on
        # the referee name and country sent in with the clientMessage
        if client_msg.startswith("ADDREFEREE,"):
            country_found = False
            same_name = False
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            if self.tournament.name == "":
                this_message = "[ERROR] - Must Create a Tournament Before Adding A Referee"
                return this_message
            for c in self.tournament.countries:
                if c.name.lower() == tokens[2].lower():
                    country_found = True
                    for r in self.tournament.referees:
                        if r.name.lower() == tokens[1].lower():
                            same_name = True
                            this_message = "[ERROR] - Referee With Name: " + tokens[1] + " Already Exists"
                            return this_message
                    if country_found is True and same_name is False:
                        self.tournament.add_ref(tokens[1], c)
                        this_message = "Successfully Added Referee"
                        return this_message
            if not country_found:
                this_message = "[ERROR] - Must Add Country Before Adding Referee"
                return this_message

        # Adds a Match
        # Adds a match to the tournament based on
        # the match date, match time, team1 name, and team2 name sent in with the clientMessage
        if client_msg.startswith("ADDMATCH,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            tokens[3] = tokens[3].replace("\r\n", "")
            tokens[4] = tokens[4].replace("\r\n", "")
            found1 = False
            found2 = False
            tokens[2] = tokens[2].replace("T", "")
            first_date = tokens[1].split('-')
            # first_date = time.strptime(tokens[1], "%y-%m-%d")
            the_time = tokens[2].split(':')
            # the_time = time.strptime(tokens[2], "%H:%M:%S")
            for f in first_date:
                print(f)
            for t in the_time:
                print(t)

            time_date = time(int(the_time[0]), int(the_time[1]), int(the_time[2]))
            start_date = datetime(int(first_date[0]), int(first_date[1]), int(first_date[2]))
            total_datetime = datetime.combine(start_date, time_date)
            for m in self.tournament.matches:
                if m.date_time == total_datetime:
                    this_message = "[ERROR]: There Is Already A Match At This Time"
                    return this_message

            team_a = Team("", "")
            team_b = Team("", "")
            tokens[4] = tokens[4].replace("\r\n", "")
            for t in self.tournament.teams:
                if t.name.lower() == tokens[3].lower():
                    team_a.name = t.name
                    team_a.country = t.country
                    found1 = True
            for t2 in self.tournament.teams:
                if t2.name.lower() == tokens[4].lower():
                    team_b.name = t2.name
                    team_b.country = t2.country
                    found2 = True
            if team_a.name.lower() == team_b.name.lower() and team_a.name != "" and team_b.name != "":
                this_message = "[ERROR]: Both Teams In Match Are The Same."
                return this_message
            if found1 is False or found2 is False:
                this_message = "[ERROR]: Team Could Not Be Found"
                return this_message
            else:
                self.tournament.add_match(total_datetime, team_a, team_b)
                this_message = "Match Added"
                return this_message

        # Assigns a Referee
        # Assigns a referee to a match based on
        # the 4 referee names, the match date, and the match time sent in with the clientMessage
        if client_msg.startswith("ASSIGNREF,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            tokens[3] = tokens[3].replace("\r\n", "")
            tokens[4] = tokens[4].replace("\r\n", "")
            tokens[5] = tokens[5].replace("\r\n", "")
            tokens[6] = tokens[6].replace("\r\n", "")
            team_a_country_name = ""
            team_b_country_name = ""
            match_good = False
            found1 = False
            nationality_check_1 = False
            found2 = False
            nationality_check_2 = False
            found3 = False
            nationality_check_3 = False
            found4 = False
            nationality_check_4 = False

            tokens[6] = tokens[6].replace("T", "")
            tokens[6] = tokens[6].replace("\r\n", "")
            first_date = tokens[5].split('-')
            the_time = tokens[6].split(':')

            time_date = time(int(the_time[0]), int(the_time[1]), int(the_time[2]))
            start_date = datetime(int(first_date[0]), int(first_date[1]), int(first_date[2]))
            match_time = datetime.combine(start_date, time_date)

            # Finding match
            for match in self.tournament.matches:
                if match.date_time == match_time:
                    team_a_country_name = match.team_a.country
                    team_b_country_name = match.team_b.country
                    match_good = True

            # Checking each referee
            for ref in self.tournament.referees:
                # Checking referee1
                name = ref.name
                name = name.replace("\r\n", "")
                country_name = ref.country.name
                country_name = country_name.replace("\r\n", "")
                if name.lower() == tokens[1].lower():
                    found1 = True
                    if country_name.lower() == team_a_country_name.lower():
                        nationality_check_1 = False
                    elif country_name.lower() == team_b_country_name.lower():
                        nationality_check_1 = False
                    else:
                        nationality_check_1 = True
                # Checking referee2
                if name.lower() == tokens[2].lower():
                    found2 = True
                    if country_name.lower() == team_a_country_name.lower():
                        nationality_check_2 = False
                    elif country_name.lower() == team_b_country_name.lower():
                        nationality_check_2 = False
                    else:
                        nationality_check_2 = True
                # Checking referee3
                if name.lower() == tokens[3].lower():
                    found3 = True
                    if country_name.lower() == team_a_country_name.lower():
                        nationality_check_3 = False
                    elif country_name.lower() == team_b_country_name.lower():
                        nationality_check_3 = False
                    else:
                        nationality_check_3 = True
                # Checking referee4
                if name.lower() == tokens[4].lower():
                    found4 = True
                    if country_name.lower() == team_a_country_name.lower():
                        nationality_check_4 = False
                    elif country_name.lower() == team_b_country_name.lower():
                        nationality_check_4 = False
                    else:
                        nationality_check_4 = True

                # One referee could not be found
                if found1 is False or found2 is False or found3 is False or found4 is False:
                    this_message = "[ERROR]: One Of The Referees Could Not Be Found"
                    return this_message
                # One of referees nationality matches that of a team
                if (
                        nationality_check_1 is False or nationality_check_2 is False or nationality_check_3 is False
                        or nationality_check_4 is False):
                    this_message = "[ERROR]: One Of The Referees Nationality Matches That of A Team"
                    return this_message
                # Match could not be found
                if match_good is False:
                    this_message = "[ERROR]: Match Could Not Be Found"
                    return this_message

                # Match was found, and referees were checked
                if (
                        found1 is True and found2 is True and found3 is True and found4 is True and
                        nationality_check_1 is True and nationality_check_2 is True and
                        nationality_check_3 is True and nationality_check_4 is True):
                    self.tournament.add_ref_to_match(match_time, tokens[1])
                    self.tournament.add_ref_to_match(match_time, tokens[2])
                    self.tournament.add_ref_to_match(match_time, tokens[3])
                    self.tournament.add_ref_to_match(match_time, tokens[4])
                    this_message = "Referees Assigned."
                    return this_message

        # Adds a Player
        # Adds a player to a team based on
        # the player name, age, height, weight, and the team name sent in with the clientMessage
        if client_msg.startswith("ADDPLAYER,"):
            tokens[1] = tokens[1].replace("\r\n", "")
            tokens[2] = tokens[2].replace("\r\n", "")
            tokens[3] = tokens[3].replace("\r\n", "")
            tokens[4] = tokens[4].replace("\r\n", "")
            tokens[5] = tokens[5].replace("\r\n", "")
            found = False
            tokens[5] = tokens[5].replace("\r\n", "")
            if self.tournament.name == "":
                this_message = "[ERROR] - Must Create a Tournament Before Adding A Player"
                return this_message

            for t in self.tournament.teams:
                if t.name.lower() == tokens[5].lower():
                    for p in t.teamPlayers:
                        if p.name.lower() == tokens[1].lower():
                            this_message = "[ERROR] - Player Already Exists In Team"
                            return this_message
                    self.tournament.add_player(tokens[1], int(tokens[2]), float(tokens[3]), float(tokens[4]))
                    t.add_player(tokens[1], int(tokens[2]), float(tokens[3]), float(tokens[4]))
                    found = True
                    this_message = "Successfully Added Player To Team"
                    return this_message
            if found is False:
                this_message = "[ERROR] - Could Not Find Team"
                return this_message

        # Records Score of a Match
        # Records the score of a match based on
        # the date of the match, the time, the score of team_a, and the score of team_b sent in with the clientMessage
        if client_msg.startswith("RECORDSCORE,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

        # Adds Player to a Lineup
        if client_msg.startswith("ADDPLAYERTOLINEUP,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

        # ----------------------------------------------------------------------------------------

        # PUBLIC APPLICATION
        # ----------------------------------------------------------------------------------------

        # Updates Upcoming Matches
        # Updates upcoming matches based on
        # the match date and the current date
        if client_msg.startswith("UPDATEUPCOMING,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

        # Updates Matches On Date
        # Updates matches for a specific date based on
        # the match date sent in with the client message
        if client_msg.startswith("UPDATEDATE,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

        # Updates Matches For Team
        # Updates matches for a given team based on
        # the team name given with the client message
        if client_msg.startswith("UPDATETEAM,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

        # Updates Lineups For a Match
        # Updates the lineups for a match based on
        if client_msg.startswith("LINEUPFORMATCH,"):
            this_message = "[ERROR] - NOT IMPLEMENTED"
            return this_message

    def display_message(self, message):
        print("[SER]" + message)
