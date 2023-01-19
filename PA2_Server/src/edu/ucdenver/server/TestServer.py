from socket import socket, AF_INET, SOCK_STREAM
# --------------------------------------------------------------------
#                    TESTING PYTHON-JAVA CONNECTION
# --------------------------------------------------------------------


server_sock = socket(AF_INET, SOCK_STREAM)  # create socket object

host = "localhost"
port = 8001
server_sock.bind((host, port))  # bind to the port
server_sock.listen(5)  # wait for client connection

while True:
    client_sock, addr = server_sock.accept()  # establish connection with client
    print(f"\nGot connection from {addr}")

    msg = client_sock.recv(1024)  # receive msg from socket, returns bytes obj repr msg recvd

    if msg[2:] == b'Hello Server':
        print("Client Connected")

    client_msg = msg[2:].decode(encoding="utf-8")
    print(f"Message from Client: {client_msg} ")



else:
    print("No msg received")

# --------------------------------------------------------------------
