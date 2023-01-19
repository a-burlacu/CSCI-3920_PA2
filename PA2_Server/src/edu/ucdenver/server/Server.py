import _thread
import threading
import time
from socket import socket, AF_INET, SOCK_STREAM
from _thread import *
import os
from threading import Thread
from edu.ucdenver.server import ServerApp


# from edu.ucdenver.server.ClientWorker import ClientWorker

# Server __main__ -> run_server() -> create ClientWorker threads -> start threads -> join threads -> close client socket

# --------------------------------------------------------------------
#                               SERVER
# --------------------------------------------------------------------
# region SERVER
class Server:

    def __init__(self, hostname: str = "localhost", port: int = 9888):

        self.__hostname = hostname
        self.__port = port
        self.__server_socket = None
        self.__client_socket = None
        self.__is_running = True
        self.__threads_list = []
        self.__client_count = 0

    # region setters/getters
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

    @property
    def threads_list(self):
        return self.__threads_list

    # endregion setters/getters
    # ------------------------------------------------------------------
    def run_server(self):
        self.__server_socket = socket(AF_INET, SOCK_STREAM)  # create a socket object
        self.__server_socket.bind(("localhost", 9888))  # bind to the port
        self.__server_socket.listen(5)  # queue up to 5 requests
        print("Server is running...")

        socket.settimeout(self.__server_socket, 3.0)  # stop waiting for connection if none within 3 sec
        while self.__is_running:
            try:

                self.__client_socket, addr = self.__server_socket.accept()  # wait for a client connection
                self.__client_count += 1
                print("Got a connection from {}.".format(str(addr)))  # print the client address

                # cw = ClientWorker(client_socket, self.hostname, self.port)
                cw = ClientWorker(self.__client_count, self.__client_socket, self)
                self.__threads_list.append(cw)

                cw.start()

            except Exception as e:
                print("\nERROR:", e)
                break

        cw: ClientWorker
        for cw in self.__threads_list:
            cw.disconnect_client()
            cw.join()

    # -------------------------------------------------------------------------------------
    def stop_server(self):
        print("Stopping server...")
        self.__server_socket.close()
        self.__is_running = False

    def load_from_file(self):
        # TODO: Send a string (like "LOAD") to Java client that triggers the 'Tournament.loadFromFile()' method
        pass

    def save_to_file(self):
        # TODO: Send a string ("SAVE") to Java client that triggers the 'Tournament.saveToFile()' method
        pass

    def display_menu(self):
        """Used to display the options available to the server app."""
        print("-" * 80)
        print("Server Main Menu")
        print("-" * 80)
        print("1. Start server\n"
              "2. Load data from file\n"
              "3. Save data to file\n"
              "4. Stop server\n")

        return int(input("Select option [1-4]: "))


# endregion SERVER

# --------------------------------------------------------------------
#                             CLIENT WORKER
# --------------------------------------------------------------------
# region CLIENT_WORKER

# Creating a thread class
# ClientWorker (a thread) handles all client requests ("switch:case") after it connects to server
# disconnects client socket -> Server joins threads

class ClientWorker(Thread):
    # def __init__(self, client_socket: socket, hostname: str, port: int):

    def __init__(self, client_id: int, client_socket: socket, server: Server):
        super().__init__()
        self.__id = client_id
        self.__client_socket = client_socket
        # self.__hostname = hostname
        # self.__port = port
        self.__server = server
        self.__user = None
        self.__keep_running_client = True
        self.__id = 0

    # region setters/getters
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

    # endregion setters/getters

    def process_client_request(self):
        """receive string messages from client, processes command"""
        msg = self.client_socket.recv(1024)  # receive msg from socket, returns bytes obj repr msg recvd
        if msg[2:].decode(encoding="utf-8") == "disconnect":
            self.disconnect_client()
        elif msg[2:].decode(encoding="utf-8") == "Hello Server":
            print(msg[2:].decode(encoding="utf-8"))

    def send_message(self, msg: str):
        """send string message to client"""
        msg += "\n"
        self.__client_socket.send(msg.encode("UTF-8"))

    # ---------------------------start CW thread-------------------------------------------
    def run(self):
        """while threads are running, continuously process client requests"""

        while self.__keep_running_client:
            self.process_client_request()

        self.__client_socket.close()

        for thread in self.__server.threads_list:
            if thread.id == self.__id:
                self.__server.threads_list.remove(thread)

    # -------------------------------------------------------------------------------------
    def disconnect_client(self):
        """when user disconnects, terminates loop in the run method to stop"""
        self.__keep_running_client = False
        self.send_message("STOP")

    def display_menu(self):

        """Used to display the options available to the server app."""
        print("-" * 80)
        print("Server Main Menu")
        print("-" * 80)
        print("1. Start server\n"
              "2. Load data from file\n"
              "3. Save data to file\n"
              "4. Stop server\n")

        return int(input("Select option [1-4]: "))


# endregion CLIENT_WORKER

# --------------------------------------------------------------------
#                              SERVER APP
# --------------------------------------------------------------------
# region SERVER_APP

if __name__ == "__main__":

    running_menu = True
    server = Server("localhost", 9888)

    while running_menu:
        option = server.display_menu()

        if option == 1:
            t1 = threading.Thread(Server.run_server(server))
            t1.start()

        elif option == 2:
            server.load_from_file()
        elif option == 3:
            server.save_to_file()
        elif option == 4:
            server.stop_server()  # set 'is_running' = False -> stop server
            t1.join()
            running_menu = False
        else:
            print("Invalid option, try again \n\n")

# endregion SERVER_APP
