package cmd_console;

public class GatewayMessageBuilder {

	public static final int CLIENT_AUTH_KEY_SIZE = 36; // auth_token from user_login_tokens table.

	/*
	 * typedef struct { uint8_t client_type; // type of client establishing
	 * connection. uint8_t client_id[4]; // DB ID of client (a user_id if mobile to
	 * tempo).
	 * 
	 * uint8_t client_authentication[CLIENT_AUTH_KEY_SIZE]; // auth_token from
	 * user_login_tokens table.
	 * 
	 * } GATEWAY_CLIENT_AUTHENTICATE;
	 */
	public static GatewayMessage createClientAuthenticate(byte clientType, int userID, String client_auth) {
		byte[] messagePayload = new byte[5 + CLIENT_AUTH_KEY_SIZE];

		messagePayload[0] = clientType;

		DataUtil.intToByte(userID, messagePayload, 1);

		for (int iAuth = 0; iAuth < CLIENT_AUTH_KEY_SIZE; iAuth++) {
			if (client_auth != null && client_auth.length() > iAuth) {
				messagePayload[5 + iAuth] = (byte) client_auth.charAt(iAuth);
			} else {
				messagePayload[5 + iAuth] = 'x';
			}
		}

		return new GatewayMessage(GatewayMessage.GM_TYPE_CLIENT_AUTHENTICATE, messagePayload);
	}

	/*
	 * typedef struct { uint8_t destination_type; // remote device to connect to
	 * (usually a tempo or logged in user). uint8_t destination_id[4]; // DB ID of
	 * remote target (a tempo_device_id or user_id table).
	 * 
	 * } GATEWAY_STREAM_REQUEST;
	 */
	public static GatewayMessage requestRemoteAudioStream(byte targetType, int targetID) {
		byte[] messagePayload = new byte[5];

		messagePayload[0] = targetType;
		DataUtil.intToByte(targetID, messagePayload, 1);

		return new GatewayMessage(GatewayMessage.GM_TYPE_START_AUDIO_STREAM, messagePayload);
	}

	public static GatewayMessage requestRemoteWiFiStream() {
		byte[] messagePayload = new byte[5];

		messagePayload[0] = 0;
		DataUtil.intToByte(0, messagePayload, 1);

		return new GatewayMessage(GatewayMessage.GM_TYPE_START_WIFI_STREAM, messagePayload);
	}

	// Accept a remote stream. No payload is needed.

	public static GatewayMessage acceptRemoteAudioStream() {
		return new GatewayMessage(GatewayMessage.GM_TYPE_START_AUDIO_STREAM);
	}

	// Accept a remote stream as Tempo. No payload is needed.

	public static GatewayMessage acceptRemoteAudioStream(String tempoSerial) {
		return new GatewayMessage(GatewayMessage.GM_TYPE_START_AUDIO_STREAM, tempoSerial);
	}

	// Request an IMU stream from a tempo device.

	// public static GatewayMessage requestRemoteIMUStream(int tempoDeviceID) {
	// byte[] messagePayload = new byte[5];
	//
	// messagePayload[0] = GatewayStreamInitialize.STREAM_CLIENT_TEMPO;
	// DataUtil.intToByte(tempoDeviceID, messagePayload, 1);
	//
	// return new GatewayMessage(GatewayMessage.GM_TYPE_START_IMU_STREAM,
	// messagePayload);
	// }

	// Request to Open console
	public static GatewayMessage requestRemoteConsole(int tempoDeviceID) {
		byte[] messagePayload = new byte[5];
		messagePayload[0] = 1;// STREAM_CLIENT_TEMPO;
		DataUtil.intToByte(tempoDeviceID, messagePayload, 1);
		return new GatewayMessage(GatewayMessage.GM_TYPE_START_CONSOLE_STREAM, messagePayload);
	}

	/*
	 * typedef struct { uint8_t packet_type; // packet type - 1 = audio data, 2 = no
	 * data, 3 = end stream. uint8_t audio_format; // audio data format - 1 = 16 bit
	 * linear uint8_t packet_num[2]; // sequence number of packet to avoid
	 * duplicates.
	 * 
	 * // audio data buffer (1 / 20th second) linear. uint8_t
	 * audio_data[AUDIO_LINEAR_16_PACKET_SIZE];
	 * 
	 * } GATEWAY_AUDIO_DATA;
	 */
	public static GatewayMessage createAudioData(short sequenceNum, short[] audioData) {
		byte[] messagePayload = new byte[4 + audioData.length * 2];

		messagePayload[0] = 1; // Audio data.
		messagePayload[1] = 1; // 16-bit linear

		DataUtil.shortToByte(sequenceNum, messagePayload, 2); // sequence num.

		// audioData = genTone();

		for (int iData = 0; iData < audioData.length; iData++) {
			DataUtil.shortToByte(audioData[iData], messagePayload, 4 + iData * 2);
		}

		return new GatewayMessage(GatewayMessage.GM_TYPE_AUDIO_DATA, messagePayload);
	}

	/*
	 * typedef struct { uint8_t json_data[variable size];
	 * 
	 * } GATEWAY_JSON_DATA;
	 */

	public static GatewayMessage createJsonRequest(String jsonMessage) {
		byte[] jsonBytes = jsonMessage.getBytes();

		return new GatewayMessage(GatewayMessage.GM_TYPE_JSON_REQUEST, jsonBytes);
	}

}
