

class Player:
    """keeps track of the _name, _age, _height, and _weight of a player"""

    def __init__(self, name: str, age: int, height: float, weight: float):
        """initializing player object"""
        self._name = name
        self._age = age
        self._height = height
        self._weight = weight

    @property
    def name(self):
        """returning the name of the player"""
        return self._name

    @property
    def age(self):
        """returning the age of the player"""
        return self._age

    @property
    def height(self):
        """returning the height of the player"""
        return self._height

    @property
    def weight(self):
        """returning the weight of the player"""
        return self._weight

    def __str__(self):
        """representing player object as a string"""
        string = 'Name: {:s} || Age: {:s} || Height: {:s} || Weight: {:s}'.format(self._name, self._age, self._height,
                                                                     self._weight)
        return string
