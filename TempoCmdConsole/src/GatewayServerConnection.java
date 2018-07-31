import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class GatewayServerConnection {
	static private final int READ_TIMEOUT = 20000; // timeout read after 20 seconds.

	public static final byte REQUEST_HEADER_SIZE = 12; // Request header size.
	public static final byte RESPONSE_HEADER_SIZE = 8; // Response header size.

	private String s_serverIP;
	private int s_serverPort;
	private String m_serverName;

	// This is our socket connection.

	protected Socket m_socket;

	// This is our socket input stream to read data.

	protected InputStream m_socketIn;

	// This is our socket output stream to write data.

	protected OutputStream m_socketOut;

	// Flag to indicate if connection is alive.

	protected boolean m_isConnectionAlive = false;

	// Initialize connection to Gateway server. This should be the US server in
	// region 01 and
	// the CA server is region 02.

	public GatewayServerConnection(String serverIP, int serverPort) {
		s_serverIP = serverIP;
		s_serverPort = serverPort;
	}

	public void setServerName(String serverName) {
		m_serverName = serverName;
	}

	public String getServerName() {
		return m_serverName;
	}

	// Open server connection and set socket parameters.
	// Returns true if connection opened successfully.

	public boolean connect() {

		try {
			// Create the socket. If we can't connect here this throws an exception.

			m_socket = new Socket(InetAddress.getByName(s_serverIP), s_serverPort);

			// Set read timeout. This really only impacts how long we wait for a request/
			// response in a round trip message. Streaming interfaces will just re-try.

			m_socket.setSoTimeout(READ_TIMEOUT);

			// Allow small packets to send immediately.

			m_socket.setTcpNoDelay(true);

			// Get our input and output socket streams.

			m_socketIn = m_socket.getInputStream();
			m_socketOut = m_socket.getOutputStream();

			m_isConnectionAlive = true;

			return true;
		} catch (Exception ex) {
			System.out.printf("Server Conn Failed.", ex.getMessage());
			return false;
		}
	}

	public boolean getIsConnectionAlive() {
		return m_isConnectionAlive;
	}

	// Send a gateway request and wait for immediate response.
	// This will return null if the READ_TIMEOUT is exceeded.

	public GatewayResponse sendGatewayRequest(GatewayMessage gatewayMessage) {
		// Convert gateway message class to binary data.

		return sendGatewayRequest(gatewayMessage.getFinalMessage());
	}

	// Send a binary request and wait for immediate response.

	public GatewayResponse sendGatewayRequest(byte[] binaryMessage) {
		byte[] responseHdr = new byte[RESPONSE_HEADER_SIZE];

		GatewayResponse gatewayResponse = null;

		try {
			// Write out the request.

			m_socketOut.write(binaryMessage);
			m_socketOut.flush();

			// Read the gateway response.
			// If we exceed READ_TIMEOUT this will throw an exception and we get a null
			// response.
			// If this returns -1, it indicates the socket was closed by peer.

			int readAmount = readSocket(responseHdr);

			if (readAmount == RESPONSE_HEADER_SIZE) {
				// We have read the header but may have additional data to read.

				short respLength = DataUtil.byteToShort(responseHdr, 0);

				// Allocate buffer for additional data.

				byte[] responsePayload = new byte[respLength - RESPONSE_HEADER_SIZE];

				// Loop and read until buffer is filled. If we hit timeout it will
				// throw an exception and exit loop.

				int bodyReadCount = readSocket(responsePayload);

				// Verify that we read the full amount.
				if (bodyReadCount == responsePayload.length) {
					// Wrap the data into a gateway response class.

					gatewayResponse = new GatewayResponse(responseHdr, responsePayload);
				} else {
					throw new Exception("Body read incorrect length.");
				}
			} else {
				throw new Exception("Header read incorrect length.");
			}
		} catch (SocketTimeoutException exTimeout) {
			// Timed out on read so retuning null.

			System.out.printf("Gateway Request", "Read request timed out.");
		} catch (Exception ex) {
			// Some other exception, likely a closed socket.

			System.out.printf("Gateway Request", ex.getMessage());

			// Close our local resources.

			close();
		}

		return gatewayResponse;
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

	// Close our connection.

	public void close() {
		m_isConnectionAlive = false;

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
			// Ignore exceptions.
		}
	}
}
