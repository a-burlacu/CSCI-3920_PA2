


class ClientWorker:
    # Constructor
    def __init__(self, connection, tournament, id):
        self.connection = connection
        self.id = id
        self.tournament = tournament



    def process_client_request(self, msg):
        print(msg)

    def display_message(self, message):
        print("[SER]" + message)
