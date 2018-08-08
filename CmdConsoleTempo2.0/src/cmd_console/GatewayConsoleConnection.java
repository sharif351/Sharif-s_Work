package cmd_console;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GatewayConsoleConnection extends Thread {
	public static final byte RESPONSE_HEADER_SIZE = 8; // Response header size.

	// We can convert the connection to a stream reader by starting the thread and
	// sending messages to
	// our handler stream reader.

	protected ConsoleCallBackInterface m_consoleCallback;

	// This is our socket connection.

	protected Socket m_socket;

	// This is our socket input stream to read data.

	protected InputStream m_socketIn;

	// This is our socket output stream to write data.

	protected OutputStream m_socketOut;

	// Flag to indicate if connection is alive.

	protected boolean m_isConnectionAlive;

	// Flag used to shut down running read thread for streaming data.

	protected boolean m_shutdown;

	// Take over a standard connection and turn it into a bi-directional stream.
	// Send asynchronous responses back over StreamCallbackInterface callback
	// gatewayResponseAvailable().

	public GatewayConsoleConnection(GatewayServerConnection originalConnection,
			ConsoleCallBackInterface consoleCallback) {
		// Copy data from original connection.

		m_socket = originalConnection.m_socket;
		m_socketIn = originalConnection.m_socketIn;
		m_socketOut = originalConnection.m_socketOut;
		m_isConnectionAlive = originalConnection.m_isConnectionAlive;

		// Save our callback reference.

		m_consoleCallback = consoleCallback;

		// Start our async thread to watch for incoming messages.

		m_shutdown = false;

		setPriority(MAX_PRIORITY);

		start();
	}

	public boolean getIsConnectionAlive() {
		return m_isConnectionAlive;
	}

	// Send a gateway one way stream request without waiting for a response.

	public void sendGatewayStreamRequest(GatewayMessage gatewayMessage) {
		try {
			// Write out the request.

			m_socketOut.write(gatewayMessage.getFinalMessage());
			m_socketOut.flush();
		} catch (Exception ex) {
			// Looks like a closed socket, free resources and shutdown reader.
			close();
		}
	}

	/**
	 * This is our main data read loop that will read until we are asked to
	 * shut-down, or until the socket is closed remotely.
	 */

	public void run() {
		try {
			// Keep looping until we are shut down by the session.

			while (m_shutdown == false) {
				try {
					// Read the data.
					byte[] responseHdr = new byte[RESPONSE_HEADER_SIZE];

					// Read our first 8 bytes to get the full response header.
					int headerReadCount = readSocket(responseHdr);

					// Make sure we have a length field.
					if (headerReadCount == RESPONSE_HEADER_SIZE) {
						short respLength = DataUtil.byteToShort(responseHdr, 0);

						byte[] responsePayload = new byte[respLength - RESPONSE_HEADER_SIZE];

						int bodyReadCount = readSocket(responsePayload);

						// Verify that we read the full amount.
						if (bodyReadCount == responsePayload.length) {
							// Notify of available data.
							m_consoleCallback
									.gatewayResponseAvailable(new GatewayResponse(responseHdr, responsePayload));
						} else {
							System.out.println("Nothing to do but shutdown connection...");
							m_shutdown = true;
						}
					} else if (headerReadCount == -1) {
						System.out.println("Looks like a closed socket.");
						m_shutdown = true;
					}
				} catch (SocketTimeoutException ex) {
					// ignore timeout exception.
				}
			}
		} catch (Exception ex) {
			// We had some type of exception (often a socket close). Notify our parent, and
			// allow thread to exit.
			// If this is the first error reported, we may enter a re-connect loop before
			// this thread exits.
			m_shutdown = true;

			m_consoleCallback.networkException("Network Exception", ex);
		}

		// Close connection and free resources. We may already be closed but just to be
		// sure.

		close();
	}

	// Read from the socket and handle partial reads.

	private int readSocket(byte[] buffer) throws IOException {
		int bodyReadCount = 0;

		while (bodyReadCount < buffer.length) {
			int remainingCount = buffer.length - bodyReadCount;
			// Read the rest of the message.
			int thisReadAmount = m_socketIn.read(buffer, bodyReadCount, remainingCount);

			if (thisReadAmount <= 0) {
				// Looks like closed socket.
				return thisReadAmount;
			}

			bodyReadCount += thisReadAmount;
		}

		return bodyReadCount;
	}

	// Close our connection and shut down reader if it's running.
	// This routine is synchronized since read and write threads
	// might call this at the same time and we only want first thread
	// to close the sockets (second thread will see sockets set to null).

	synchronized public void close() {
		if (m_isConnectionAlive == true) {
			m_isConnectionAlive = false;
			m_shutdown = true;

			try {
				if (m_socketIn != null) {
					m_socketIn.close();
					m_socketIn = null;
				}
				if (m_socketOut != null) {
					m_socketOut.close();
					m_socketOut = null;
				}

				if (m_socket != null) {
					m_socket.close();
					m_socket = null;
				}
			} catch (Exception ex) {

			}

			// Notify callback interface we are shut down.

			m_consoleCallback.connectionClosing();
		}
	}
}
