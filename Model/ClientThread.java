/**
 * Thread handling the communication between server and client. Each client has its own thread
 * @author Dag Brynildsen Tholander
 * @version 2022-03-01
 */

package Model;

import java.io.*;
import java.net.*;

public class ClientThread extends Thread {
    private Socket socket;
    private Server server;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    /**
     * Creates a new clien thread and initializes its input/ouput streams
     * @author Dag Brynildsen Tholander
     * @param _socket
     * @param _server
     */
    public ClientThread(Socket _socket, Server _server) {
        this.socket = _socket;
        this.server = _server;
        try {
            ois = new ObjectInputStream(socket.getInputStream());
            oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println("Error in ClientThread constructor: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Main loop of the client thread.
     * If the server isn't in a match, join the server. Else recieve data sent by client
     * @author Dag Brynildsen Tholander
     */
    public void run() {
        try {
            // If we arent in a match, add new users to the lobby
            if (!server.inMatch()) {
                // If someone has connected, add them 
                String username = (String) ois.readObject();
                Player newUser = new Player(username);
                server.addUser(newUser);
            }
            // Loop that runs while in a match. 
            while (true) {
                try {
                    Object recieved = ois.readObject();
                    // If we recieve transmitData, then we have to handle it
                    if (recieved instanceof TransmitData) {
                        server.handleCard((TransmitData) recieved);
                    }
                } catch (ClassNotFoundException e) {
                    System.out.println("Error recieving : " + e.getMessage());
                }
            }
        } catch (IOException ex) {
            System.out.println("Error in UserThread: " + ex.getMessage());
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            System.out.println("Error recieving username in UserThread: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * sends an object to the client
     * @param message
     * @author Dag Brynildsen Tholander
     */
    public void sendObject(Object message) {
        try {
            oos.writeObject(message);
            oos.reset();
        } catch (IOException e) {
            System.out.println("Error in userThread, can't send object : " + e.getMessage());
        }
    }
}
