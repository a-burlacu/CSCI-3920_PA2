import datetime
import json
from typing import List
from edu.ucdenver.Tournament.Country import Country
from edu.ucdenver.Tournament.Team import Team
from edu.ucdenver.Tournament.Player import Player
from edu.ucdenver.Tournament.Referee import Referee
from edu.ucdenver.Tournament.Lineup import Lineup
from edu.ucdenver.Tournament.Match import Match


class Tournament:
    """keeps track of a tournaments containing participating __participating_countries, __list_players,
    __list_teams, __list_referees, __list_matches, and lineups"""
    __name = ""
    __start_date = ""

    # --------------------------------------------------
    #                  constructor
    # --------------------------------------------------
    def __init__(self, name: str, start_date: datetime, end_date: datetime):
        """initializing tournament object"""
        self.__name = name
        self.__start_date = start_date
        self.__end_date = end_date
        self.__teamPlayers: List[Player] = []
        self.__list_teams: List[Team] = []
        self.__list_players: List[Player] = []
        self.__list_matches: List[Match] = []
        self.__list_line_ups: List[Lineup] = []
        self.__list_referees: List[Referee] = []
        self.countries: List[Country]= []
        self.__participating_countries: List[Country] = []
        self.__filename : "default"

    # --------------------------------------------------
    #               getters and setters
    # --------------------------------------------------
    def set_name(self, name: str):
        """setting tournament __name"""
        self.__name = name

    def set_start_date(self, __start_date: datetime):
        """setting start date"""
        self.__start_date = __start_date

    def set_end_date(self, __end_date: datetime):
        """setting end date"""
        self.__end_date = __end_date


    @property
    def filename(self):
        return self.__filename

    @filename.setter
    def filename(self, filename):
        self.__filename = filename


    # --------------------------------------------------
    #               class methods: admin
    # --------------------------------------------------

    def save_file(self, filename: str):

        temp = []
        for team in self.__list_teams:
            jsonStr = json.dumps(team.__dict__)
            temp.append(jsonStr)

        new_list = []
        for t in temp:
            final = {"Team Name": t[0], "Country": t[1], "Squad": t[3]}
            new_list.append(final)

        with open(self.__filename + '.json', 'w') as save_file:
            json.dump(new_list, save_file, indent=1)


        # var = self.__dict__
        #
        #
        # with open(self.__filename + '.json', 'w') as save_file:
        #     json.dump(var, save_file, indent=1)

    def load_file(self, filename: str):
        pass

    def add_country(self, country_name: str):
        """adding a participating country to the tournament"""
        country_exists = False

        # checking if country exists
        for country in self.countries:
            if country.name == country_name:
                country_exists = True

        # if country exists raise a value error
        if country_exists:
            raise ValueError("ERROR! This country already exists.")

        # if country does not exist create and add new country
        elif not country_exists:
            self.countries.append(Country(country_name))

    def add_team(self, team_name: str, country: str):
        """"adding a team to the tournament"""
        team_exists = False
        country_exists = False

        # check to see if team exists
        for team in self.__list_teams:
            if team.name == team_name:
                team_exists = True

        # check to see if country exists
        for c in self.__participating_countries:
            if c.name == country:
                country_exists = True

        # if team does not exist and country does exist add the team
        if team_exists is False and country_exists is True:
            self.__list_teams.append(Team(team_name, country))

        # if team does exist or country does not exist raise a value error
        elif team_exists is True or country_exists is False:
            if team_exists is True:
                raise ValueError("ERROR! This team already exists.")
            elif not country_exists:
                raise ValueError("ERROR! This country does not exist.")

    def add_referee(self, name: str, country: str):
        """adding a referee to the tournament"""
        referee_exists = False
        country_exists = False

        # checking to see if the referee already exists
        for referee in self.__list_referees:
            if referee.name == name:
                referee_exists = True

        # checking to see if the country exists
        for c in self.__participating_countries:
            if c.name == country:
                country_exists = True

        # if the country exists but the referee does not, add the referee
        if country_exists is True and referee_exists is False:
            self.__list_referees.append(Referee(name, country))

        # if the country does not exist or the referee already exists raise the appropriate error
        elif country_exists is False or referee_exists is True:
            if country_exists is False:
                raise ValueError("ERROR! This country does not exist.")
            elif referee_exists is True:
                raise ValueError("ERROR! This referee already exists.")

    def add_player(self, team_name: str, name: str, age: int, height: float, weight: float):
        """adding a player to a team in the tournament"""
        team_exists = False
        player_exists = False

        # checking to see if team exists and if player exists and adding player if team exists
        # and player does not already exist
        for team in self.__list_teams:
            if team.name == team_name:
                team_exists = True
                for player in team.squad:
                    player_exists = True
                    if player.name == name:
                        self.__list_players.append(team.add_player(name, age, height, weight))

        # raising the correct value error
        if team_exists is False or player_exists is True:
            if team_exists is False:
                raise ValueError("ERROR! This team does not exist.")
            elif player_exists is True:
                raise ValueError("ERROR! This player already exists.")

    def add_match(self, date_time: datetime, team_a: str, team_b: str):
        """adding a match involving two teams in the tournament"""
        team_a_exists = False
        team_b_exists = False

        # checking to see if team A and team B are equal
        # if they are equal, raise a value error
        if team_a.lower() == team_b.lower():
            raise ValueError("ERROR! Team A and Team B cannot be the same team.")

        # checking to see if team A and team B exist in the tournament
        # adding the match if they both exist
        for team1 in self.__list_teams:
            if team1.name == team_a:
                team_a_exists = True
                for team2 in self.__list_teams:
                    if team2.name == team_b:
                        team_b_exists = True
                        self.__list_matches.append(Match(date_time, team1, team2))

        # if one of the teams
        if team_a_exists is False or team_b_exists is False:
            raise ValueError("ERROR! One of these teams does not exist")

    def add_referee_to_match(self, date_time: datetime, referee_name: str):
        """adding a referee to a match occurring in the tournament"""
        referee_exists = False
        match_exists = False

        # checking to see if the match and referee exist before adding the
        # referee to the match
        for match in self.__list_matches:
            if match.date_time == date_time:
                match_exists = True
                for referee in self.__list_referees:
                    referee_exists = True
                    if referee.name.lower() is referee_name:
                        match.add_referee(referee)

        # raising the appropriate value error when referee or match doesn't exist
        if referee_exists is False or match_exists is False:
            if referee_exists is False:
                raise ValueError("ERROR! This referee does not exist.")
            elif match_exists is False:
                raise ValueError("ERROR! This match does not exist.")

    def add_player_to_match(self, date_time: datetime, team: str, player: str):
        """adding a player to a match lineup"""
        match_exists = False
        player_exists = False
        team_exists = False

        # checking if the match exists
        for match in self.__list_matches:
            if match.date_time == date_time:
                match_exists = True
                for t in self.__list_teams:
                    if t.name == team:
                        team_exists = True
                        # checking of player exists
                        for p in t.squad:
                            if p.name == player:
                                player_exists = True
                                match.add_player(p, t)

        if match_exists is False or player_exists is False or team_exists is False:
            if match_exists is False:
                raise ValueError("ERROR! This match does not exist.")
            elif team_exists is False:
                raise ValueError("ERROR! This team does not exist.")
            elif player_exists is False:
                raise ValueError("ERROR! This player does not exist")

    # Other functions
    def set_match_score(self, date_time: datetime, team_a_score: int, team_b_score: int):
        """setting the match score of a completed match"""
        match_exists = False

        for match in self.__list_matches:
            if match.date_time == date_time:
                match_exists = True
                match.set_score_team_a(team_a_score)
                match.set_score_team_b(team_b_score)
                match.set_match_score(team_a_score, team_b_score)

        # raising a value error and displaying message if match does not exist
        if match_exists is False:
            raise ValueError("ERROR! This match does not exist.")

    # --------------------------------------------------
    #            class methods: user/public
    # --------------------------------------------------

    def get_upcoming_matches(self):
        """returning matches that have not yet occurred in the tournament"""
        return self.__list_matches

    def get_matches_on(self, match_date: datetime):
        """returning matches that occur on a particular day of the tournament"""
        matches_on: List[Match] = []
        for match in self.__list_matches:
            m_date = match.date_time
            m_date = m_date.datetime.date()
            if m_date == match_date:
                matches_on.append(match)

        return matches_on

    def get_matches_for(self, team_name: str):
        """returning matches that involve a particular team participating in the tournament"""
        matches_for: List[Match] = []
        for match in self.__list_matches:
            team_a = match.team_a()
            team_b = match.team_b()
            if team_name.lower() == team_a.__name or team_name.lower() == team_b.__name:
                matches_for.append(match)
        return matches_for

    def get_match_line_ups(self, match_date: datetime):
        """returning the lineup for matches taking place on a specific day of the tournament"""
        match_lineups: List[Lineup] = []
        for match in self.__list_matches:
            if match.date_time == match_date:
                for line_up in self.__list_line_ups:
                    if match.team_a.__name == line_up.team.__name:
                        match_lineups.append(line_up)
                    if match.team_b.__name == line_up.team.__name:
                        match_lineups.append(line_up)

        return match_lineups

    # def add_lineup(self, team):
    # """adding a lineup to the tournament"""
    # self.__list_line_ups.append(Lineup(team))

    def __str__(self):
        """representing the tournament object as a string"""
        string = 'This is the {:s} tournament.|'.format(self.__name)
        return string
