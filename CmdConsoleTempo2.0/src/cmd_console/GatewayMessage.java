package cmd_console;

import java.io.InputStream;
import java.io.OutputStream;

public class GatewayMessage {
	public static final int TEMPO_SERIAL_SIZE = 6; // Tempo serial number.

	// Our message types.

	public static final byte GM_TYPE_KEEP_ALIVE = 0; // No action, keep alive connection and check for work.

	public static final byte GM_TYPE_GET_TIME_CONFIG = 1; // Get current timestamp and device basic configuration.
	public static final byte GM_TYPE_DATA_PACKETS = 2; // One or more data or log packets from a tempo.

	public static final byte GM_TYPE_CLIENT_AUTHENTICATE = 3; // Packets being passed through from a hub.

	public static final byte GM_TYPE_PASS_THROUGH = 4; // Packets being passed through from a hub.

	public static final byte GM_TYPE_START_AUDIO_STREAM = 5; // Start a stream of audio data.
	public static final byte GM_TYPE_START_IMU_STREAM = 6; // Start bi-directional IMU stream.
	public static final byte GM_TYPE_START_CONSOLE_STREAM = 7; // Start a stream of console data.
	public static final byte GM_TYPE_START_WIFI_STREAM = 8; // Start a stream of wifi heat map data.

	public static final byte GM_TYPE_AUDIO_DATA = 10; // Streaming audio data packets.
	public static final byte GM_TYPE_IMU_DATA = 11; // Packet contains IMU data.
	public static final byte GM_TYPE_CONSOLE_DATA = 12; // Packet contains IMU data.

	public static final byte GM_TYPE_STOP_STREAM = 15; // Force stop a data stream.

	public static final byte GM_TYPE_JSON_REQUEST = 20; // Unstructured JSON request.

	public static final byte FLAG_IS_REMOTE_CLIENT = (byte) 0x80; // Header flag indicating we are a remote client (not
																	// a tempo or hub).

	private byte m_messageType;
	private byte m_messageVersion;

	private byte[] m_headerData = new byte[GatewayServerConnection.REQUEST_HEADER_SIZE];
	private byte[] m_payloadData = null;

	private int m_totalLength = GatewayServerConnection.REQUEST_HEADER_SIZE;

	/**
	 * This is our socket input stream to read data.
	 */

	private InputStream m_socketIn;

	/**
	 * This is our socket output stream to write data.
	 */

	private OutputStream m_socketOut;

	// Create a basic message with no initial payload.

	public GatewayMessage(byte messageType) {
		m_messageType = messageType;
		m_messageVersion = 0;
	}

	// Create a message as a Tempo device.

	public GatewayMessage(byte messageType, String tempoSerial) {
		m_messageType = messageType;
		m_messageVersion = 0;
		addTempoSerial(tempoSerial);
	}

	// Create a message with a payload.

	public GatewayMessage(byte messageType, byte[] payloadData) {
		m_messageType = messageType;
		m_messageVersion = 0;
		addMessagePayload(payloadData);
	}

	// Create a message as a Tempo device with a payload.

	public GatewayMessage(byte messageType, String tempoSerial, byte[] payloadData) {
		m_messageType = messageType;
		m_messageVersion = 0;
		addTempoSerial(tempoSerial);
		addMessagePayload(payloadData);
	}

	public GatewayMessage(byte messageType, byte messageVersion) {
		m_messageType = messageType;
		m_messageVersion = messageVersion;
	}

	public GatewayMessage(byte messageType, byte messageVersion, byte[] payloadData) {
		m_messageType = messageType;
		m_messageVersion = messageVersion;
		addMessagePayload(payloadData);
	}

	public void addTempoSerial(String serial) {
		if (serial.length() == TEMPO_SERIAL_SIZE) {
			for (int iDigit = 0; iDigit < TEMPO_SERIAL_SIZE; iDigit++) {
				m_headerData[iDigit + 6] = DataUtil.fromHex(serial.charAt(iDigit));
			}
		}
	}

	public void addMessagePayload(byte[] payloadData) {
		m_totalLength = GatewayServerConnection.REQUEST_HEADER_SIZE + payloadData.length;
		m_payloadData = payloadData;
	}

	/*
	 * typedef struct { uint8_t message_length[2]; // total length of message
	 * including header and message_length bytes. uint8_t message_type; // type of
	 * message to the gateway. uint8_t message_flags; // bit flags in header. //
	 * Replaced with bits set in message_flags //uint8_t message_version : 4; //
	 * version number of this type of message. //uint8_t is_high_priority : 1; // is
	 * a high priority message. //uint8_t is_from_hub : 1; // this is coming from a
	 * hub. //uint8_t is_encrypted : 1; // the message payload will be encrypted
	 * after header. //uint8_t is_remote_client : 1; // this is coming from a remote
	 * client like a mobile device. uint8_t sequence_num[2]; // sequence number of
	 * message to avoid duplicates. // for GM_TYPE_GET_TIME_CONFIG, this is device
	 * build_num[2]; uint8_t serial_num[SERIAL_NUM_SIZE]; // 6 byte serial number of
	 * tempo or hub.
	 * 
	 * } GATEWAY_MESSAGE_HEADER;
	 */

	public byte[] getFinalMessage() {
		DataUtil.shortToByte((short) m_totalLength, m_headerData, 0);
		m_headerData[2] = m_messageType;
		m_headerData[3] = m_messageVersion;
		m_headerData[3] |= FLAG_IS_REMOTE_CLIENT;

		// no serial number or sequence number for remote client messages.

		if (m_payloadData != null) {
			byte[] headerAndPayload = new byte[m_headerData.length + m_payloadData.length];

			for (int iByte = 0; iByte < m_headerData.length; iByte++) {
				headerAndPayload[iByte] = m_headerData[iByte];
			}

			for (int iByte = 0; iByte < m_payloadData.length; iByte++) {
				headerAndPayload[iByte + m_headerData.length] = m_payloadData[iByte];
			}

			return headerAndPayload;
		} else {
			// No payload, just a header.
			return m_headerData;
		}
	}
}
