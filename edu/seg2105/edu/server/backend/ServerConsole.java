package edu.seg2105.edu.server.backend;

import edu.seg2105.client.common.ChatIF;
import ocsf.server.ConnectionToClient;

import java.io.*;
import java.util.Scanner;

public class ServerConsole implements ChatIF {
	// Class variables
	final public static int DEFAULT_PORT = 5555;
	private static EchoServer server;
	Scanner fromConsole;

	// Constructor
	public ServerConsole(int port) {
		server = new EchoServer(port);
		fromConsole = new Scanner(System.in);
	}

	// Instance Methods

	public void accept() {
		try {
			String message;

			while (true) {
				message = fromConsole.nextLine();
				if (message.startsWith("#")) {
					handleMessage(message);
				} else {
					display(message);
					server.sendToAllClients("SERVER MESSAGE> " + message);
				}
			}
		} catch (Exception ex) {
			System.out.println("Unexpected error while reading from console!");
		}
	}

	private void handleMessage(String command) {
		String plain = command.toLowerCase().trim();
		String[] split = command.split(" ");
		if (plain.equals("#quit")) {
			server.setqoc("q");
			closeServer();
			System.exit(0);
		} else if (plain.equals("#stop")) {
			server.stopListening();
		} else if (plain.contains("#close")) {
			server.setqoc("c");
			closeServer();
		} else if (plain.contains("#setport")) {
			try {
				server.setPort(Integer.valueOf(split[1]));
				display("Port has been set to " + split[1]);
			} catch (ArrayIndexOutOfBoundsException e) {
				display("No arguement is given for the command.");
			}
		} else if (plain.equals("#start")) {
			if (server.isListening()) {
				display("The server is still running and listening.");
			} else {
				try {
					server.listen();
				} catch (IOException e) {
					display("Could not start listening on the server.");
				}
			}
		} else if (plain.equals("#getport")) {
			display("Current Port: " + server.getPort());
		} else {
			display("Invalid command");
		}
	}

	public void closeServer() {
		do {
			server.stopListening();
			try {
				Thread.sleep(10); // tiny delay lets listener thread finish
			} catch (InterruptedException e) {
			}
		} while (server.isListening());
		try {
			server.close();
		} catch (IOException e) {
			display("Could not close the server.");
		}
	}

	@Override
	public void display(String message) {
		System.out.println("SERVER MSG> " + message);
	}

	// Class Methods
	public static void main(String[] args) {
		int port = 0;

		try {
			port = Integer.parseInt(args[0]);
		} catch (ArrayIndexOutOfBoundsException e) {
			port = DEFAULT_PORT;
		} catch (NumberFormatException ne) {
			port = DEFAULT_PORT;
		}

		ServerConsole chat = new ServerConsole(port);
		try {
			server.listen();
		} catch (Exception e) {
			System.out.println("ERROR - Could not listen for clients!");
		}
		chat.accept();

	}

}
