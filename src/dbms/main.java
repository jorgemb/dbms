package dbms;

import GUI.GUI;
import connection.Server;
import java.awt.EventQueue;

/**
 *
 * @author eddycastro
 */
public class main {

	private static int serverPort = 4545;

	/**
	 * Prints usage
	 */
	private static void printUsage() {
		System.out.println("Usage: java -jar DBMS.jar [Parameters]");
		System.out.println("\tNo parameters: \t- Direct client");
		System.out.println("\t-s [PORT] \t- Server mode with given port or default (" + serverPort + ") if not specified.");
	}

	/**
	 * Entry point
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length == 0) { // No arguments

				EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						GUI window = new GUI();
						window.setVisible(true);
					}
				});
			} else if (args[0].equals("-s")) {  // Server form
				if (args.length > 1) {
					serverPort = Integer.parseInt(args[1]);
				}
				Server server = new Server(serverPort);
				server.init();
			} else { // ERROR
				printUsage();
			}
		} catch (Exception ex) {
			System.out.println("ERROR: " + ex.getMessage());
			printUsage();
		}
	}
}
