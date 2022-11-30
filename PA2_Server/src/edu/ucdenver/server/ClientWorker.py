from threading import Thread


# Creating a thread class
class ClientWorker(Thread):

    def __init__(self):
        super().__init__()
        # ClientWorker initialization here
    #
    # # like implementing "Runnable" in Java
    # def run(self) -> None:
    #     # thread code here
    #
    #     if __name__ == "__main__":
    #         thread = InputReader()
    # thread = ClientWorker()
    # thread.start()
    #
    # threads = [ClientWorker(temp) for x in x_list] # create threads
    #
    # while thread.is_alive():
    #     #do something