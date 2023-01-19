# from edu.ucdenver.server.Server import Server
#
#
# def main():
#     running_menu = True
#     server = Server("localhost", 9888)
#
#     while running_menu:
#         option = server.display_menu()
#         if option == 1:
#             # server.run_server()
#             server.start()
#         elif option == 2:
#             server.load_from_file()
#         elif option == 3:
#             server.save_to_file()
#         elif option == 4:
#             server.stop_server()  # set 'is_running' = False -> stop server
#             running_menu = False
#             server.join()
#         else:
#             print("Invalid option, try again \n\n")
#
#
# if __name__ == "__main__":
#     main()
