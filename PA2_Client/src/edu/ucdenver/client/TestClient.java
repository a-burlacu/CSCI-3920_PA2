package edu.ucdenver.client;

import java.io.DataOutputStream;
import java.net.Socket;

public class TestClient {

    public static void main(String[] args) {

        try {
            Socket clientSock = new Socket("localhost", 1000);
            DataOutputStream output = new DataOutputStream(clientSock.getOutputStream());
            output.writeUTF("Hello Server");
            output.flush();
            output.close();
            clientSock.close();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
