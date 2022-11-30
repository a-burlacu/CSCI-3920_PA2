from socket import socket, AF_INET, SOCK_STREAM

# class Server:
#     def __init__(self):
#         pass
#
#     server_socket = socket(AF_INET,SOCK_STREAM) # crreate a socket object
#     server_socket.bind(('localhost',9999))        # bind to the port
#     server_socket.listen(5)                      # queue up to 5 requests
#
#     while True:
#         client_socket, addr = server_socket.accept()    # wait for a client connection
#         print("Got a connection from {}.".format(str(addr))  # print the client address
#
#         msg = "Thank you for connecting\r\n"
#
#         client_socket.send(msg.encode('ascii')) # transmit sequence of bytes from one socket to another
#
#         # have to use 'encode' since strings are sent as bytes
#         # other side needs to decode it
#         #
#         # example on server side:
#         #
#         #  received_msg = server_socket.recvmsg(buff_size) [0]
#         #  str_msg = received_msg.decode('ascii')
#         #  recvmsg() returns other information, the message is the first element[0] in tuple
#
#
#         client_socket.close()
#
#     server_socket.close()


# --------------------------------------------------------------------
#                    TESTING PYTHON-JAVA CONNECTION
# --------------------------------------------------------------------

server_sock = socket(AF_INET,SOCK_STREAM) # create socket object
host = "localhost"
port = 1000
server_sock.bind((host, port))  # bind to the port
server_sock.listen(5)           # wait for client connection

while True:
    client_sock, addr = server_sock.accept() # establish connection with client
    print(f"Got connection from {addr}")

    msg = client_sock.recv(1024)
    print(msg)

    if msg.decode() == "Hello Server":
        print("Hello Client")
    else:
        print("No msg received")
