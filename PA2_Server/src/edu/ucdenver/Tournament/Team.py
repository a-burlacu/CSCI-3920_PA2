from typing import List

from edu.ucdenver.Tournament import Country
from edu.ucdenver.Tournament.Player import Player


class Team:
    """keeps track of the name, __country, and players on a team"""

    def __init__(self, name: str, country: Country):
        """initializing team object"""
        self.__name = name
        self.__country = country
        self.__squad: List[Player] = []

    @property
    def name(self):
        """returning the name of the team"""
        return self.__name

    @name.setter
    def name(self, n: str):
        self.__name = n

    @property
    def country(self):
        """returning the teams country"""
        return self.__country

    @country.setter
    def country(self, c: Country):
        self.__country = c

    @property
    def squad(self):
        """returning the list of players on the team"""
        return self.__squad

    def add_player(self, name: str, age: int, height: float, weight: float):
        """adding a player to the team"""
        new_player = Player(name, age, height, weight)
        self.__squad.append(new_player)











