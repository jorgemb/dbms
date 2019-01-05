package connection;

import userInterface.ServerPrinter;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Basic server
 *
 * @author Jorge
 */
public class Server {

	private final int port;

	private Thread consoleThread;
	private Thread clientsThread;

	/**
	 * Port constructor
	 *
	 * @param port Numeric port to use
	 */
	public Server(int port) {
		this.port = port;
	}

	/**
	 * Initializes the server
	 */
	public void init() throws IOException {
		// Thread for direct commands to the server
		consoleThread = new Thread() {
			//<editor-fold defaultstate="collapsed" desc="Console management">
			@Override
			public void run() {
				try {
					BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
					while (!isInterrupted()) {
						if (stdIn.ready()) {
							String command = stdIn.readLine();
							doCommand(command);
						}
					}
				} catch (IOException ex) {
					System.out.println("ERROR: " + ex.getMessage());
				}
			}
			//</editor-fold>
		};
		consoleThread.start();

		// Client management
		clientsThread = new Thread() {
			//<editor-fold defaultstate="collapsed" desc="Manejo de clientes">
			@Override
			public void run() {
				try {
					ServerSocket serverSocket = new ServerSocket(port);
					serverSocket.setSoTimeout(1000);

					// Checks client connection
					while (!isInterrupted()) {
						try {
							try (
								Socket client = serverSocket.accept();
								BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
								BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
								ServerPrinter printer = new ServerPrinter(bw)) {

								// Current client
								ClientManager manager = new ClientManager(br, bw);
								manager.manageClient();
								bw.flush();
							}

						} catch (InterruptedIOException ex) {
						}
					}
				} catch (Exception ex) {
					System.out.println("ERROR IN SERVER: " + ex.getMessage());
				}

				closeServer();
			}
			//</editor-fold>
		};
		clientsThread.start();
	}

	/**
	 * Releases server resources
	 */
	synchronized private void closeServer() {
		consoleThread.interrupt();
		clientsThread.interrupt();
	}

	/**
	 * Does a command given directly to the server
	 *
	 * @param command Command to execute
	 * @return true if the server can continue
	 */
	synchronized private void doCommand(String command) {
		String upperCommand = command.toUpperCase();

		if (upperCommand.equals("CLOSE")) {
			System.out.println("<Closing server>");
			closeServer();
		} else {
			System.out.println("ERROR: Unrecognized command - " + command);
		}

	}
}
