import datetime
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
        self.__participating_countries: List[Country] = []

    # --------------------------------------------------
    #               getters and setters
    # --------------------------------------------------
    def set_name(self, __name: str):
        """setting tournament __name"""
        self.__name = __name

    def set_start_date(self, __start_date: datetime):
        """setting start date"""
        self.__start_date = __start_date

    def set_end_date(self, __end_date: datetime):
        """setting end date"""
        self.__end_date = __end_date

    # --------------------------------------------------
    #               class methods: admin
    # --------------------------------------------------

    def add_country(self, country_name: str):
        """adding a participating country to the tournament"""
        self.__participating_countries.append(Country(country_name))

    def add_team(self, team_name: str, country: str):
        """"adding a team to the tournament"""
        self.__list_teams.append(Team(team_name, country))

    def add_referee(self, __name: str, country: str):
        """adding a referee to the tournament"""
        self.__list_referees.append(Referee(__name, country))

    def add_player(self, team_name: str, __name: str, age: int, height: float, weight: float):
        """adding a player to a team in the tournament"""
        self.__list_players.append(Player(__name, age, height, weight))

    def add_match(self, date_time: datetime, team_a: str, team_b: str):
        """adding a match involving two teams in the tournament"""
        for team1 in self.__list_teams:
            if team1.name is team_a:
                for team2 in self.__list_teams:
                    if team2.name is team_b:
                        self.__list_matches.append(Match(date_time, team1, team2))

    def add_referee_to_match(self, date_time: datetime, referee_name: str):
        """adding a referee to a match occurring in the tournament"""
        for match in self.__list_matches:
            if match.date_time == date_time:
                for referee in self.__list_referees:
                    if referee.name.lower() == referee_name.lower():
                        match.add_referee(referee)

    def add_player_to_match(self, date_time: datetime, team: str, player: str):
        """adding a player to a match lineup"""
        if not self.__list_line_ups:
            print("Creating new lineup.")
            l_u = Lineup(team)
            l_u.add_player(player)
            self.__list_line_ups.append(l_u)
            return
        for line_up in self.__list_line_ups:
            if line_up.team == team:
                line_up.add_player(player)
                return
        print("Creating new lineup.")
        line_up = Lineup(team)
        line_up.add_player(player)
        self.__list_line_ups.append(line_up)

    # Other functions
    def set_match_score(self, date_time: datetime, team_a_score: int, team_b_score: int):
        """setting the match score of a completed match"""
        for match in self.__list_matches:
            if match.date_time == date_time:
                match.set_score_team_a(team_a_score)
                match.set_score_team_b(team_b_score)
                match.set_match_score(team_a_score, team_b_score)

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

    def add_lineup(self, team):
        """adding a lineup to the tournament"""
        self.__list_line_ups.append(Lineup(team))

    def __str__(self):
        """representing the tournament object as a string"""
        string = 'This is the {:s} tournament.|'.format(self.__name)
        return string
