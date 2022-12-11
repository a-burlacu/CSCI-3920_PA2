package edu.ucdenver.client;

import edu.ucdenver.tournament.Country;
import edu.ucdenver.tournament.Tournament;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.function.ToIntBiFunction;

public class TestClient {
    public static void main(String[] args) {

        Socket clientSock = null;
        DataOutputStream output = null;
//        ObjectOutputStream objectOutput = null;
        Country country = new Country("USA");
        try {
            clientSock = new Socket("localhost", 9888);
            output = new DataOutputStream(clientSock.getOutputStream());
//            objectOutput = new ObjectOutputStream(clientSock.getOutputStream());

            output.writeUTF("Hello Server");
            output.flush();

//            objectOutput.writeObject(country);
//            objectOutput.flush();


//            output.close();
//            clientSock.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
//                objectOutput.close();
                output.close();
                clientSock.close();
            } catch (IOException | NullPointerException e) {
                System.err.println(e);
            }

        }
    }
}
