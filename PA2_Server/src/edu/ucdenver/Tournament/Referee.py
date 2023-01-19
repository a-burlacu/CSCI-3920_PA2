from edu.ucdenver.Tournament import Country


class Referee:
    """keeps track of the name and country of a referee"""

    def __init__(self, name: str, country: Country):
        """initializing referee object"""
        self.__name = name
        self.__country = country

    @property
    def name(self):
        """returning the referees name"""
        return self.__name

    @property
    def country(self):
        """returning the referees country"""
        return self.__country


