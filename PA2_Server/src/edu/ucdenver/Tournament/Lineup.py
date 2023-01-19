from typing import List

from edu.ucdenver.Tournament import Team, Player


class Lineup:
    """keeps track of the team lineups for a match"""

    # Constructor
    def __init__(self, team: Team):
        """initializing lineup object"""
        self.__team = team
        self.__list_of_players = []

    @property
    def team(self):
        return self.__team

    @property
    def list_of_players(self):
        return self.__list_of_players

    def add_player(self, player: Player):
        """adding a player to the lineup"""
        self.__list_of_players.append(player)

    def player_str(self):
        p_string = ""
        for player in self.__list_of_players:
            p_string = p_string + player.name + ","

        return p_string
