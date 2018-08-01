package cmd_console;

public class CpGatewayConnectivity {

	private final static String DEFAULT_GATEWAY_IP = "gateway.carepredict.com";
	private final static int DEFAULT_GATEWAY_PORT = 443;

	public static final byte STREAM_CLIENT_TEMPO = 1; // We are connecting to a Tempo device.
	public static final byte STREAM_CLIENT_USER = 2; // We are connecting to a logged in user (on Mobile or Web).
	public static final byte STREAM_CLIENT_TEST = 3; // For development use, stream is from test client.

	// hard coded key used for test clients.
	private final static String TEST_AUTH_KEY = "f1c68c5a-e925-4e75-a932-5618e5a4f0ae";

	private boolean isGateWayConnacted = false;

	public CpGatewayConnectivity() {
		System.out.println("CpGatewayConnectivity class started:");
	}

	public static GatewayConsoleConnection InitializeConnection(int tempoDeviceID,
			ConsoleCallBackInterface consoleCallback) throws Exception {

		GatewayServerConnection serverConnection = new GatewayServerConnection(DEFAULT_GATEWAY_IP,
				DEFAULT_GATEWAY_PORT);

		// Connect to remote server.
		GatewayConsoleConnection returnGatewayConsole = null;

		if (serverConnection.connect()) {
			System.out.println("CarePredict Gateway Connected.");

			// Authenticate us as a test client with hard-coded authentication key.
			GatewayMessage clientAuthenticate = GatewayMessageBuilder.createClientAuthenticate(STREAM_CLIENT_TEST, 0,
					TEST_AUTH_KEY);

			// Send a synchronous request to the server.
			GatewayResponse authResponse = serverConnection.sendGatewayRequest(clientAuthenticate);

			if (authResponse == null) {
				throw new Exception("Timed out waiting for response from gateway.");
			}

			if (authResponse.getMessageType() == GatewayResponse.GR_TYPE_SUCCESS) {
				// Convert to a stream connection with our class as the stream callback.

				System.out.println("User authenticated. Now requesting to open Console to Tempo: " + tempoDeviceID);

				returnGatewayConsole = new GatewayConsoleConnection(serverConnection, consoleCallback);

				// Now request the stream connection with the target device.

				GatewayMessage requestStream = GatewayMessageBuilder.requestRemoteConsole(tempoDeviceID);

				returnGatewayConsole.sendGatewayStreamRequest(requestStream);

			} else {
				throw new Exception("Unable to authenticate user to gateway.");
			}

		} else {
			throw new Exception("Unable to connect to gateway: " + DEFAULT_GATEWAY_IP);
		}

		return returnGatewayConsole;
	}
}
