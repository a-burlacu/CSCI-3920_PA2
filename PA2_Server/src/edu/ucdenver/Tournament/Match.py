import datetime

from edu.ucdenver.Tournament import Referee
from edu.ucdenver.Tournament.Lineup import Lineup
from edu.ucdenver.Tournament.Team import Team


class Match:
    """keeps track of the teams, referees, date, and scores of a match"""

    # Constructor
    def __init__(self, date_time: datetime, team_a: Team, team_b: Team):
        """initializing match object"""
        self.__date_time = date_time
        self.__scoreTeamA = 0
        self.__scoreTeamB = 0

        self.__team_a = Lineup(team_a)
        self.__team_b = Lineup(team_b)
        self.__match_referees = list()

    @property
    def team_a(self):
        return self.__team_a

    @property
    def team_b(self):
        return self.__team_b

    @property
    def match_referees(self):
        return self.__match_referees

    @property
    def date_time(self):
        return self.__date_time

    # Add functions
    def add_player(self, player, team):
        team.add_player(player.name, player.age, player.height, player.weight)

    def add_referee(self, referee: Referee):
        self.__match_referees.append(referee)

    def set_match_score(self, score_team_a: int, score_team_b: int):
        self.__scoreTeamA = score_team_a
        self.__scoreTeamB = score_team_b

    def get_date_time(self) -> datetime:
        return self.__date_time

    def set_score_team_a(self, score_team_a: int):
        self.__scoreTeamA = score_team_a

    def set_score_team_b(self, score_team_b: int):
        self.__scoreTeamB = score_team_b

    def __str__(self):
        now = datetime.now()
        string = ""
        if self.__date_time > now:
            string = 'Match Date: {:s} || Team 1: {:s} || Team2: {:s} || Team1 Score: {:d} || Team2 Score: {:d}'.format(
                self.__date_time, self.__team_a.team.name, self.__team_b.team.name,
                self.__scoreTeamA, self.__scoreTeamB)
        else:
            string = 'Match Date: {:s} || Team 1: {:s} || Team2: {:s}'.format(
                self.__date_time, self.__team_a.team.name, self.__team_b.team.name)
        return string
