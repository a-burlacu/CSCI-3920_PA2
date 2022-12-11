# from edu.ucdenver.server.ServerApp import main
# from threading import Thread
# import socket
#
#
# # TODO: must handle all client requests
#
# # Creating a thread class
# # ClientWorker (a thread) handles all client requests ("switch:case") after it connects to server
# # disconnects client socket -> Server joins threads
# class ClientWorker(Thread):
#     def __init__(self, client_socket: socket, hostname: str, port: int):
#         super().__init__()
#         self.__client_socket = client_socket
#         self.__hostname = hostname
#         self.__port = port
#         self.__user = None
#         self.__keep_running_client = True
#         self.__id = 0
#
#
#     # --------------------------------------------------------------------
#     #                         getters & setters
#     # --------------------------------------------------------------------
#
#     @property
#     def id(self):
#         return self.__id
#
#     @id.setter
#     def id(self, client_id: int):
#         self.__id = client_id
#
#     @property
#     def client_socket(self):
#         return self.__client_socket
#
#     @client_socket.setter
#     def client_socket(self, client_socket: socket):
#         self.__client_socket = client_socket
#
#     @property
#     def hostname(self):
#         return self.__hostname
#
#     @hostname.setter
#     def hostname(self, hostname: str):
#         self.__hostname = hostname
#
#     @property
#     def port(self):
#         return self.__port
#
#     @port.setter
#     def hostname(self, port: int):
#         self.__port = port
#
#     @property
#     def keep_running_client(self):
#         return self.__keep_running_client
#
#     @keep_running_client.setter
#     def keep_running_client(self, state: bool):
#         self.__keep_running_client = state
#
#     # --------------------------------------------------------------------
#     #                           class methods
#     # --------------------------------------------------------------------
#     def process_client_request(self):
#         """receive string messages from client, processes command"""
#         msg = self.client_socket.recv(1024)  # receive msg from socket, returns bytes obj repr msg recvd
#         if msg[2:].decode(encoding="utf-8") == "disconnect":
#             self.disconnect_client()
#         elif msg[2:].decode(encoding="utf-8") == "Hello Server":
#             print(msg[2:].decode(encoding="utf-8"))
#
#
#     def send_message(self, msg: str):
#         """send string message to client"""
#         self.__client_socket.send(msg.encode("UTF-8"))
#
#     # --------------------------------------------------------------------
#     def run(self):
#         """while threads are running, continuously process client requests"""
#
#         while self.__keep_running_client:
#             main()
#             self.process_client_request()
#
#         self.__client_socket.close()
#
#         # for thread in self.__threads:
#         #     if thread.id == self.__id:
#         #         self.__threads.remove(thread)
#
#     def disconnect_client(self):
#         """when user disconnects, terminates loop in the run method to stop"""
#         self.__keep_running_client = False
