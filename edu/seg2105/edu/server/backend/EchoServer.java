package edu.seg2105.edu.server.backend;
// This file contains material supporting section 3.7 of the textbook:

import java.io.IOException;

// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 */
public class EchoServer extends AbstractServer {
  // Class variables *************************************************

  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  final public static String key = "loginID";
  private String qoc;
  // Constructors ****************************************************

  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) {
    super(port);
  }

  // Instance methods ************************************************

  /**
   * This method handles any messages received from the client.
   *
   * @param msg    The message received from the client.
   * @param client The connection from which the message originated.
   */
  public void handleMessageFromClient(Object msg, ConnectionToClient client) {
    String message = (String) msg;
    Object hasLoggedIn = client.getInfo("hasLoggedIn");

    if (hasLoggedIn == null) {
      System.out.println("Message received: " + msg + " from " + client.getInfo(key));
      if (message.startsWith("#login")) {

        client.setInfo("hasLoggedIn", true);
        message.trim();
        try {
          String[] login = message.split(" ");
          client.setInfo(key, login[1]);
        } catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("loginID was not given");
        }
      }
      System.out.println(client.getInfo(key) + " has logged on.");
      try {
        client.sendToClient(client.getInfo(key) + " has logged on.");
      } catch (IOException e) {
        System.out.println("Could not send message to client");
      }
      return;
    }

    if (message.equals("#login")) {
      try {
        client.sendToClient("You are already logged in. Terminating connection.");
        client.sendToClient("#close");
        client.close();
      } catch (IOException e) {
        System.out.println("Could not send to client.");
      }

    } else if (message.equals("#quit") || msg.equals("#logoff")) {
      try {

        client.close();
      } catch (IOException e) {
        System.out.println("Could not remove client from server.");
      }
    } else {
      System.out.println("Message received: " + msg + " from " + client.getInfo(key));
      this.sendToAllClients(client.getInfo(key) + ": " + msg);
    }
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server starts listening for connections.
   */
  protected void serverStarted() {
    System.out.println("Server listening for connections on port " + getPort());
  }

  /**
   * This method overrides the one in the superclass. Called
   * when the server stops listening for connections.
   */
  protected void serverStopped() {
    System.out.println("Server has stopped listening for connections.");
  }

  // Class methods ***************************************************

  /**
   * This method is responsible for the creation of
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on. Defaults to 5555
   *                if no argument is entered.
   */
  public static void main(String[] args) {
    int port = 0; // Port to listen on

    try {
      port = Integer.parseInt(args[0]); // Get port from command line
    } catch (Throwable t) {
      port = DEFAULT_PORT; // Set port to 5555
    }

    EchoServer sv = new EchoServer(port);

    try {
      sv.listen(); // Start listening for connections
    } catch (Exception ex) {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }

  protected void setqoc(String s) {
    qoc = s;
  }

  @Override
  protected void clientConnected(ConnectionToClient client) {
    System.out.println("A new client has connected to the server.");
  }

  @Override
  protected void clientDisconnected(ConnectionToClient client) {
    super.clientDisconnected(client);
    System.out.println(client.getInfo(key) + " has disconnected.");

  }

  @Override
  protected void serverClosed() {
    if (qoc.equals("q")) {
      System.out.println("Server has been closed. Terminating server.");
    } else if (qoc.equals("c")) {
      System.out.println("Server has been closed.");
    }
    qoc = "";
  }
}
// End of EchoServer class
