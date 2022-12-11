from socket import socket, AF_INET, SOCK_STREAM
from _thread import *
import os
from threading import Thread


# from edu.ucdenver.server.ClientWorker import ClientWorker


# TODO: server must initialize threads (ClientWorker objects)
# Server __main__ -> run_server() -> create ClientWorker threads -> start threads -> join threads -> close client socket


def display_menu():

    """Used to display the options available to the server app."""
    print("=" * 80)
    print("Server Main Menu\n")

    print("1. Start server")
    print("2. Load data from file")
    print("3. Save data to file")
    print("4. Stop server\n")

    return int(input("Select option [1-4]: "))


class Server:
    threads = []

    def __init__(self, hostname: str = "localhost", port: int = 9888):
        self.__hostname = hostname
        self.__port = port
        self.__server_socket = None
        self.__is_running = True

    @property
    def hostname(self):
        return self.__hostname

    @property
    def port(self):
        return self.__port

    @property
    def server_socket(self):
        return self.__server_socket

    @server_socket.setter
    def server_socket(self, sock: socket):
        self.__server_socket = sock

    @property
    def is_running(self):
        return self.__is_running

    @is_running.setter
    def is_running(self, value: bool):
        self.__is_running = value

    # --------------------------------------------------------------------
    def run_server(self):
        self.server_socket = socket(AF_INET, SOCK_STREAM)  # create a socket object
        self.server_socket.bind(("localhost", 9888))  # bind to the port
        self.server_socket.listen(5)  # queue up to 5 requests
        print("Server is running...")
        self.connect_client()

    def connect_client(self):
        display_menu()
        socket.settimeout(self.server_socket, 3.0)
        try:
            while self.__is_running:
                client_socket, addr = self.server_socket.accept()  # wait for a client connection

                print("Got a connection from {}.".format(str(addr)))  # print the client address

                # cw = ClientWorker(client_socket, self.hostname, self.port)
                cw = ClientWorker(client_socket, Server(self.hostname, self.port))

                ClientWorker.run(cw)
                Server.threads.append(cw)

                cw.start()


        except Exception as e:
            print("\nERROR:", e)

        cw: ClientWorker
        for cw in Server.threads:
            cw.send_message("STOP")
            cw.join()

    def stop_server(self):
        print("Stopping server...")
        self.__is_running = False
        self.__server_socket.close()

    def load_from_file(self):
        # TODO: Send a string (like "LOAD") to Java client that triggers the 'Tournament.loadFromFile()' method
        pass

    def save_to_file(self):
        # TODO: Send a string ("SAVE") to Java client that triggers the 'Tournament.saveToFile()' method
        pass


# --------------------------------------------------------------------
#                             CLIENT WORKER
# --------------------------------------------------------------------

# TODO: must handle all client requests

# Creating a thread class
# ClientWorker (a thread) handles all client requests ("switch:case") after it connects to server
# disconnects client socket -> Server joins threads
class ClientWorker(Thread):
    # def __init__(self, client_socket: socket, hostname: str, port: int):
    def __init__(self, client_socket: socket, server: Server):
        super().__init__()
        self.__client_socket = client_socket
        # self.__hostname = hostname
        # self.__port = port
        self.__server = server
        self.__user = None
        self.__keep_running_client = True
        self.__id = 0

    # --------------------------------------------------------------------
    #                         getters & setters
    # --------------------------------------------------------------------

    @property
    def id(self):
        return self.__id

    @id.setter
    def id(self, client_id: int):
        self.__id = client_id

    @property
    def client_socket(self):
        return self.__client_socket

    @client_socket.setter
    def client_socket(self, client_socket: socket):
        self.__client_socket = client_socket

    # @property
    # def hostname(self):
    #     return self.__hostname
    #
    # @hostname.setter
    # def hostname(self, hostname: str):
    #     self.__hostname = hostname

    # @property
    # def port(self):
    #     return self.__port

    # @port.setter
    # def hostname(self, port: int):
    #     self.__port = port

    @property
    def keep_running_client(self):
        return self.__keep_running_client

    @keep_running_client.setter
    def keep_running_client(self, state: bool):
        self.__keep_running_client = state

    # --------------------------------------------------------------------
    #                           class methods
    # --------------------------------------------------------------------
    def process_client_request(self):
        """receive string messages from client, processes command"""
        msg = self.client_socket.recv(1024)  # receive msg from socket, returns bytes obj repr msg recvd
        if msg[2:].decode(encoding="utf-8") == "disconnect":
            self.disconnect_client()
        elif msg[2:].decode(encoding="utf-8") == "Hello Server":
            print(msg[2:].decode(encoding="utf-8"))

    def send_message(self, msg: str):
        """send string message to client"""
        self.__client_socket.send(msg.encode("UTF-8"))

    # --------------------------------------------------------------------
    def run(self):
        """while threads are running, continuously process client requests"""

        while self.__keep_running_client:
            self.process_client_request()

        self.__client_socket.close()

        # for thread in self.__threads:
        #     if thread.id == self.__id:
        #         self.__threads.remove(thread)

    def disconnect_client(self):
        """when user disconnects, terminates loop in the run method to stop"""
        self.__keep_running_client = False


# --------------------------------------------------------------------
#                              SERVER APP
# --------------------------------------------------------------------

if __name__ == "__main__":
    running_menu = True
    server = Server("localhost", 9888)

    while running_menu:
        option = display_menu()
        if option == 1:
            server.run_server()
        elif option == 2:
            server.load_from_file()
        elif option == 3:
            server.save_to_file()
        elif option == 4:
            server.stop_server()  # set 'is_running' = False -> stop server
            running_menu = False
        else:
            print("Invalid option, try again \n\n")

# --------------------------------------------------------------------
#                    TESTING PYTHON-JAVA CONNECTION
# --------------------------------------------------------------------

# server_sock = socket(AF_INET, SOCK_STREAM)  # create socket object
#
# host = "localhost"
# port = 1000
# server_sock.bind((host, port))  # bind to the port
# server_sock.listen(5)  # wait for client connection
#
# while True:
#     client_sock, addr = server_sock.accept()  # establish connection with client
#     print(f"Got connection from {addr}")
#
#     msg = client_sock.recv(1024)  # receive msg from socket, returns bytes obj repr msg recvd
#
#     if msg[2:] == b'Hello Server':
#         print("Hello Client")
#
#     print(msg[2:].decode(encoding="utf-8"))

#     print("Hello Client")
# else:
#     print("No msg received")

# --------------------------------------------------------------------
