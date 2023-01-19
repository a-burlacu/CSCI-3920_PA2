import datetime
import json
from typing import List

from edu.ucdenver.Tournament.Country import Country
from edu.ucdenver.Tournament.Lineup import Lineup
from edu.ucdenver.Tournament.Match import Match
from edu.ucdenver.Tournament.Player import Player
from edu.ucdenver.Tournament.Referee import Referee
from edu.ucdenver.Tournament.Team import Team


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
        self.__list_teams: List[Team] = [Team("USTEAM", Country("USA")), Team("CanadaTeam",Country("Canada"))]
        self.__list_players: List[Player] = []
        self.__list_matches: List[Match] = []
        self.__list_line_ups: List[Lineup] = []
        self.__list_referees: List[Referee] = []
        self.__participating_countries: List[Country] = [Country("USA"), Country("Canada")]

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

        # --------------------------------------------------
        #               class methods: admin
        # --------------------------------------------------

    def save_file(self, filename: str):
        # var = self.__dict__
        # with open(filename + '.json', 'w') as save_file:
        #     # json.dump(var, save_file, indent=1)
        #     print(json.dumps(self.__dict__, indent=1), file=save_file)

        # obj = var["_Tournament__name"]
        # new_list = []
        # for obj in var:
        #     format_dict = {"tournament-name": obj, "start-date": obj[1], "end-date": obj[2]}
        #     new_list.append(format_dict)

        temp = []
        for team in self.__list_teams:
            jsonStr = json.dumps(team.__dict__)
            temp.append(jsonStr)

        for c in self.__participating_countries:
            jsonStr = json.dumps(c.__dict__)
            temp.append(jsonStr)


        new_list = []
        for t in temp:
            final = {"Team Name": t[0], "Country": t[1],}
            new_list.append(final)

        with open(filename + '.json', 'w') as save_file:
            json.dump(new_list, save_file, indent=1)



if __name__ == "__main__":
    tournament = Tournament("Test Tournament", '2022:12:11', 2022-12-12)
    tournament.save_file("test")

