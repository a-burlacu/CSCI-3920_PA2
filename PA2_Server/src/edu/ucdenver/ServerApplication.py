from edu.ucdenver.Tournament import Tournament
from edu.ucdenver.Server import Server


class ServerApplication:
    # Runs the server
    tournament = Tournament.Tournament("", "", "")
    server = Server(10003, 7)
    server.run_server()
