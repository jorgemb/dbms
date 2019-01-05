package connection;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Jorge
 */
public class DummyClient {

	public static void main(String[] args) throws Exception {
		Socket kkSocket = new Socket("localhost", 4545);

		String input;
		try (
			PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
			BufferedReader in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()))) {
			out.println("USE DATABASE proyecto;");
			out.println("SELECT * FROM factura;");
			out.println("<FIN>");

			while ((input = in.readLine()) != null) {
				System.out.println(input);
			}
		}

	}
}
