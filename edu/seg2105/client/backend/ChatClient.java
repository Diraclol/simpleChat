// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package edu.seg2105.client.backend;

import ocsf.client.*;

import java.io.*;

import edu.seg2105.client.common.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 */
public class ChatClient extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of
	 * the display method in the client.
	 */
	ChatIF clientUI;
	private String loginID;
	private String qol;
	// Constructors ****************************************************

	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 */

	public ChatClient(String loginID, String host, int port, ChatIF clientUI)
			throws IOException {
		super(host, port); // Call the superclass constructor
		this.loginID = loginID;
		this.clientUI = clientUI;

		if (loginID == null || loginID == "") {
			quit();
		} else {
			openConnection();
		}

	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		String message = (String) msg;
		if (message.contains("#close")) {
			try {
				sendToServer("#logoff");

			} catch (IOException e) {
				clientUI.display("Could not close connection");
			}

		} else {
			if (message.startsWith("SERVER MESSAGE>")) {
				System.out.println(message);
			} else if (message.endsWith("has logged on.")) {
				System.out.println(message);
			} else if (message.contains(">")) {
				System.out.println(message);
			} else {
				clientUI.display(msg.toString());
			}
		}

	}

	/**
	 * This method handles all data coming from the UI
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String message) {
		try {
			if (message.startsWith("#")) {
				handleMessage(message);
			} else {
				sendToServer(message);
			}

		} catch (IOException e) {
			clientUI.display("Could not send message to server. Terminating client.");
			quit();
		}
	}

	private void handleMessage(String command) {
		String plain = command.toLowerCase().trim();
		String[] split = command.split(" ");
		if (plain.equals("#quit")) {
			try {
				if (isConnected()) {
					sendToServer(plain);
					qol = "q";
					quit();
				} else {
					System.out.println("Terminating client");
					quit();
				}

				System.out.println("Client has been terminated.");
			} catch (IOException e) {
				clientUI.display("Could not terminate the client");
			}

		} else if (plain.equals("#logoff")) {
			try {
				sendToServer(plain);
				qol = "l";
				closeConnection();
			} catch (IOException e) {
				clientUI.display("Could not logoff of the server");
			}
		} else if (plain.contains("#sethost")) {
			try {
				setHost(split[1]);
				clientUI.display("Host has been set to " + split[1]);

			} catch (ArrayIndexOutOfBoundsException e) {
				clientUI.display("Arguement was not given for the command");
			}
		} else if (plain.contains("#setport")) {
			try {
				setPort(Integer.valueOf(split[1]));
				clientUI.display("Port has been set to " + split[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				clientUI.display("Arguement was not given for the command");
			}
		} else if (plain.equals("#login")) {
			if (!isConnected()) {
				try {
					openConnection();
				} catch (IOException e) {
					clientUI.display("Could not connected to server");
				}
			} else {
				try {
					sendToServer(command);
				} catch (IOException e) {
					clientUI.display("Could not send command to server");
				}
			}
		} else if (command.equals("#gethost")) {
			clientUI.display(getHost());
		} else if (command.equals("#getport")) {
			clientUI.display(String.valueOf(getPort()));
		} else {
			clientUI.display("Invalid command");
		}

	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * Implements Hook method called after the connection has been closed. The
	 * default
	 * implementation does nothing. The method may be overriden by subclasses to
	 * perform special processing such as cleaning up and terminating, or attempting
	 * to reconnect.
	 */
	@Override
	protected void connectionClosed() {
		if (qol.equals("q")) {
			System.out.println("Connection closed. Terminating client.");
			System.exit(0);
		} else if (qol.equals("l")) {
			System.out.println("Connection closed.");
		}
		qol = "";
	}

	/**
	 * Implements Hook method called each time an exception is thrown by the
	 * client's thread
	 * that is waiting for messages from the server. The method may be overridden by
	 * subclasses.
	 * 
	 * @param exception the exception raised.
	 */
	@Override
	protected void connectionException(Exception exception) {
		System.out.println("The server has shut down.");
		System.exit(0);
	}

	/**
	 * Implements Hook method called after a connection has been established. The
	 * default
	 * implementation does nothing. It may be overridden by subclasses to do
	 * anything they wish.
	 */
	@Override
	protected void connectionEstablished() {
		try {
			sendToServer("#login " + loginID);
		} catch (IOException e) {
			clientUI.display("Could not send login attempt on the client");
		}
	}
}
// End of ChatClient class
