import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/*@author David DeSimone
 * Basic Echo client for Java. Read's users input from System.in and echos it to a server.
 * Usage: ./EchoClient.java <host name> <port>
 * 
 * 
 */
public class EchoClient {

	// The socket object we will be communicating over
	public static Socket sock;

	public static BufferedReader clientInput;
	public static BufferedReader serverInput;
	public static PrintWriter clientRes;

	public static void main(String[] args) {
		// Host name and port to be connected to
		String hostName;
		String port;
		int portNum;

		String cIn;
		String cOut;

		if (args.length != 2) {
			System.out.println("Error, not enough arguments");
			return;
		}

		hostName = args[0];
		port = args[1];
		portNum = Integer.parseInt(port);

		try {
			// Try and open the Socket
			sock = new Socket(hostName, portNum);

			// If the socket opens, open the readers/writers
			clientInput = new BufferedReader(new InputStreamReader(System.in));
			serverInput = new BufferedReader(new InputStreamReader(
					sock.getInputStream()));
			clientRes = new PrintWriter(sock.getOutputStream(), true);

			// Read the Clients input
			while ((cIn = clientInput.readLine()) != null) {
				if (cIn.equals("$") || cIn.equals("#")) {
					shutDown();
					return;
				}
				// Send the clients input to the server
				clientRes.println(cIn);

				// If we are still running, get the servers responce and print
				// it to console
				cOut = serverInput.readLine();
				System.out.println(cOut);

			}

		} catch (UnknownHostException e) {
			System.out.println("Error, could not connect to host " + hostName
					+ " on port " + portNum);
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Error, could not connect to host " + hostName
					+ " on port " + portNum);
			e.printStackTrace();
		}

	}

	/* Method used to close relevant input stream and socket connection */
	public static void shutDown() throws IOException {
		clientInput.close();
		serverInput.close();
		clientRes.close();
		sock.close();
	}

}
