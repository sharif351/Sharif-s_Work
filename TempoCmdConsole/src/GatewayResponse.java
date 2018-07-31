public class GatewayResponse {
	// Our message types.

	public static final byte GR_TYPE_FAILURE = 0; // Our last message failed to process, retry later...

	public static final byte GR_TYPE_SUCCESS = 1; // Our last message was successfully processed no additional data.

	public static final byte GR_TYPE_STREAM_IN_USE = 2; // Stream is already in use with another client.
	public static final byte GR_TYPE_STREAM_WAITING = 3; // Stream is waiting for peer to connect.
	public static final byte GR_TYPE_STREAM_ESTABLISHED = 4; // Stream is now established with peer.

	public static final byte GR_TYPE_AUDIO_DATA = 10; // Streaming audio data packets.
	public static final byte GR_TYPE_IMU_DATA = 11; // Streaming IMU data packet.
	public static final byte GR_TYPE_CONSOLE_DATA = 12; // Streaming console data packet.

	public static final byte GR_TYPE_STREAM_CLOSING = 15; // Stream is closing down (either server shutdown or peer
															// request).

	public static final byte GR_TYPE_JSON_RESPONSE = 20; // JSON response message

	// These are the further action types.

	public static final byte GACT_START_AUDIO_STREAM = 5; // Request to start audio stream.
	public static final byte GACT_START_IMU_STREAM = 6; // Request to start IMU stream.
	public static final byte GACT_START_CONSOLE_STREAM = 7; // Request to start console stream.

	private byte m_messageType;
	private byte m_messageVersion;
	private byte m_furtherActionType;

	private byte[] m_headerData;
	private byte[] m_payloadData;

	/*
	 * typedef struct { uint8_t response_length[2]; // total length of response
	 * including header and message_length. uint8_t response_type; // the type of
	 * response or failure.
	 * 
	 * uint8_t response_flags; // 8 bits of response flags.
	 * 
	 * // uint8_t response_version : 4; // version of this response type.
	 * 
	 * //uint8_t is_encrypted : 1; // true if payload is encrypted. //uint8_t
	 * is_further_action : 1; // true if further action is requested of device.
	 * //uint8_t unused_flags : 2; // set to 0, space for more bit flags.
	 * 
	 * uint8_t sequence_num[2]; // sequence number of original message to avoid
	 * duplicates. uint8_t gateway_id; // ID of the gateway server for debugging.
	 * uint8_t further_action_type; // type of action requested.
	 * 
	 * } GATEWAY_RESPONSE_HEADER;
	 */

	public GatewayResponse(byte[] headerData, byte[] payloadData) {
		m_headerData = headerData;
		m_payloadData = payloadData;

		m_messageType = headerData[2];
		m_messageVersion = (byte) (headerData[3] & 0x0f);

		m_furtherActionType = headerData[7];
	}

	public byte getMessageType() {
		return m_messageType;
	}

	public byte getMessageVersion() {
		return m_messageVersion;
	}

	public byte getFurtherActionType() {
		return m_furtherActionType;
	}

	public byte[] getMessagePayload() {
		return m_payloadData;
	}
}
