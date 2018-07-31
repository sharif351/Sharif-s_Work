package cmd_console;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

/**
 * These are simple utilities used for data conversion (e.g. hex to binary /
 * byte[] to int).
 *
 * @author dmarsyla
 * @version 1.0 - Copyright (c) 2017 - CarePredict, Inc.
 */

public class DataUtil {
	public final static char HEX_VALUE[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E',
			'F' };
	public final static String VERIFICATION_STRING = "CarePred"; // String used to verify

	// Ascii to scancode value lookup (range 32-128 ascii). Bit 0x80 indicates shift
	// key modifier.
	final public static byte SCANCODE_VALUE[] = { (byte) 0x2c, // Ascii 32 ' '
			(byte) 0x9e, // Ascii 33 '!'
			(byte) 0xb4, // Ascii 34 '"'
			(byte) 0xa0, // Ascii 35 '#'
			(byte) 0xa1, // Ascii 36 '$'
			(byte) 0xa2, // Ascii 37 '%'
			(byte) 0xa4, // Ascii 38 '&'
			(byte) 0x34, // Ascii 39 '''
			(byte) 0xa6, // Ascii 40 '('
			(byte) 0xa7, // Ascii 41 ')'
			(byte) 0xa5, // Ascii 42 '*'
			(byte) 0xae, // Ascii 43 '+'
			(byte) 0x36, // Ascii 44 ','
			(byte) 0x2d, // Ascii 45 '-'
			(byte) 0x37, // Ascii 46 '.'
			(byte) 0x38, // Ascii 47 '/'
			(byte) 0x27, // Ascii 48 '0'
			(byte) 0x1e, // Ascii 49 '1'
			(byte) 0x1f, // Ascii 50 '2'
			(byte) 0x20, // Ascii 51 '3'
			(byte) 0x21, // Ascii 52 '4'
			(byte) 0x22, // Ascii 53 '5'
			(byte) 0x23, // Ascii 54 '6'
			(byte) 0x24, // Ascii 55 '7'
			(byte) 0x25, // Ascii 56 '8'
			(byte) 0x26, // Ascii 57 '9'
			(byte) 0xb3, // Ascii 58 ':'
			(byte) 0x33, // Ascii 59 ';'
			(byte) 0xb6, // Ascii 60 '<'
			(byte) 0x2e, // Ascii 61 '='
			(byte) 0xb7, // Ascii 62 '>'
			(byte) 0xb8, // Ascii 63 '?'
			(byte) 0x9f, // Ascii 64 '@'
			(byte) 0x84, // Ascii 65 'A'
			(byte) 0x85, // Ascii 66 'B'
			(byte) 0x86, // Ascii 67 'C'
			(byte) 0x87, // Ascii 68 'D'
			(byte) 0x88, // Ascii 69 'E'
			(byte) 0x89, // Ascii 70 'F'
			(byte) 0x8a, // Ascii 71 'G'
			(byte) 0x8b, // Ascii 72 'H'
			(byte) 0x8c, // Ascii 73 'I'
			(byte) 0x8d, // Ascii 74 'J'
			(byte) 0x8e, // Ascii 75 'K'
			(byte) 0x8f, // Ascii 76 'L'
			(byte) 0x90, // Ascii 77 'M'
			(byte) 0x91, // Ascii 78 'N'
			(byte) 0x92, // Ascii 79 'O'
			(byte) 0x93, // Ascii 80 'P'
			(byte) 0x94, // Ascii 81 'Q'
			(byte) 0x95, // Ascii 82 'R'
			(byte) 0x96, // Ascii 83 'S'
			(byte) 0x97, // Ascii 84 'T'
			(byte) 0x98, // Ascii 85 'U'
			(byte) 0x99, // Ascii 86 'V'
			(byte) 0x9a, // Ascii 87 'W'
			(byte) 0x9b, // Ascii 88 'X'
			(byte) 0x9c, // Ascii 89 'Y'
			(byte) 0x9d, // Ascii 90 'Z'
			(byte) 0x2f, // Ascii 91 '['
			(byte) 0x31, // Ascii 92 '\'
			(byte) 0x30, // Ascii 93 ']'
			(byte) 0xa3, // Ascii 94 '^'
			(byte) 0xad, // Ascii 95 '_'
			(byte) 0x35, // Ascii 96 '`'
			(byte) 0x04, // Ascii 97 'a'
			(byte) 0x05, // Ascii 98 'b'
			(byte) 0x06, // Ascii 99 'c'
			(byte) 0x07, // Ascii 100 'd'
			(byte) 0x08, // Ascii 101 'e'
			(byte) 0x09, // Ascii 102 'f'
			(byte) 0x0a, // Ascii 103 'g'
			(byte) 0x0b, // Ascii 104 'h'
			(byte) 0x0c, // Ascii 105 'i'
			(byte) 0x0d, // Ascii 106 'j'
			(byte) 0x0e, // Ascii 107 'k'
			(byte) 0x0f, // Ascii 108 'l'
			(byte) 0x10, // Ascii 109 'm'
			(byte) 0x11, // Ascii 110 'n'
			(byte) 0x12, // Ascii 111 'o'
			(byte) 0x13, // Ascii 112 'p'
			(byte) 0x14, // Ascii 113 'q'
			(byte) 0x15, // Ascii 114 'r'
			(byte) 0x16, // Ascii 115 's'
			(byte) 0x17, // Ascii 116 't'
			(byte) 0x18, // Ascii 117 'u'
			(byte) 0x19, // Ascii 118 'v'
			(byte) 0x1a, // Ascii 119 'w'
			(byte) 0x1b, // Ascii 120 'x'
			(byte) 0x1c, // Ascii 121 'y'
			(byte) 0x1d, // Ascii 122 'z'
			(byte) 0xaf, // Ascii 123 '{'
			(byte) 0xb1, // Ascii 124 '|'
			(byte) 0xb0, // Ascii 125 '}'
			(byte) 0xb5 // Ascii 126 '~'
	};

	static public byte charToScancode(char oneChar) {
		int lookup = (int) oneChar - 32;

		if (lookup < 0 || lookup > 126) {
			lookup = 0;
		}

		return SCANCODE_VALUE[lookup];
	}

	static public String countryName(String countryAbbrev) {
		if (countryAbbrev.equals("US")) {
			return "United States";
		} else if (countryAbbrev.equals("CA")) {
			return "Canada";
		} else {
			return "";
		}
	}

	/**
	 * Utility routine to safely convert a string to an integer.
	 *
	 * @return The state id as an integer.
	 * @param id
	 *            - Passed in string ID.
	 */

	static public int intValue(String id) {
		int stateID = 0;

		try {
			if (id != null) {
				stateID = Integer.parseInt(id);
			}
		} catch (NumberFormatException ex) {
			// ignore.
		}

		return stateID;
	}

	/**
	 * Utility routine to safely convert a string to a char.
	 *
	 * @return The state id as a char.
	 * @param id
	 *            - in string ID.
	 */

	static public char charValue(String id) {
		if (id != null && id.length() > 0)
			return id.charAt(0);
		else
			return 0;
	}

	/**
	 * Utility routine to safely convert a string to an integer.
	 *
	 * @return The state id as an integer.
	 * @param id
	 *            - in string ID.
	 */

	static public long longValue(String id) {
		long stateID = 0;

		try {
			stateID = Long.parseLong(id);
		} catch (NumberFormatException ex) {
			// ignore.
		}

		return stateID;
	}

	/**
	 * Utility routine to safely convert a string to an integer.
	 *
	 * @return The state id as an integer.
	 * @param id
	 *            - in string ID.
	 */

	static public double doubleValue(String id) {
		double stateID = 0;

		try {
			stateID = Double.parseDouble(id);
		} catch (NumberFormatException ex) {
			// ignore.
		}

		return stateID;
	}

	/**
	 * Utility routine to safely convert a string to an integer.
	 *
	 * @return The state id as an integer.
	 * @param id
	 *            - in string ID.
	 */

	static public int hexValue(String id) {
		int stateID = 0;

		try {
			// Skip leading value.

			if (id.indexOf("0x") >= 0) {
				id = id.substring(id.indexOf("0x") + 2);
			}

			stateID = Integer.parseInt(id, 16);
		} catch (Exception ex) {
			// ignore.
		}

		return stateID;
	}

	/**
	 * Utility routine to safely convert an integer to a 8 digit Hex string.
	 *
	 * @return The 8 digit hex value.
	 * @param id
	 *            - Passed in ID.
	 */

	static public String hexValue(int id) {
		try {
			String hexValue = Integer.toHexString(id).toUpperCase();
			int strLen = hexValue.length();

			if (strLen == 1) {
				return "0000000" + hexValue;
			} else if (strLen == 2) {
				return "000000" + hexValue;
			} else if (strLen == 3) {
				return "00000" + hexValue;
			} else if (strLen == 4) {
				return "0000" + hexValue;
			} else if (strLen == 5) {
				return "000" + hexValue;
			} else if (strLen == 6) {
				return "00" + hexValue;
			} else if (strLen == 7) {
				return "0" + hexValue;
			} else {
				return hexValue;
			}
		} catch (Exception ex) {
			return "00000000";
		}
	}

	/**
	 * Calculate the days since 1970.
	 */

	private static final long MILLS_IN_MINUTE = 60000;

	private static final int MINUTES_IN_DAY = (60 * 24);

	public static int getDaysSince1970(String dateString) {
		return getDaysSince1970(Date.valueOf(dateString));
	}

	public static int getDaysSince1970(java.util.Date currDate) {
		int minutesSince1970 = getMinutesSince1970(currDate);

		return minutesSince1970 / MINUTES_IN_DAY;
	}

	public static int getDaysSince1970(long millisSince1970) {
		int minutesSince1970 = getMinutesSince1970(millisSince1970);

		return minutesSince1970 / MINUTES_IN_DAY;
	}

	public static int getMinutesSince1970(java.util.Date currDate) {
		// Find our configuration data.

		long millsSince1970 = currDate.getTime();

		int minutesSince1970 = (int) (millsSince1970 / MILLS_IN_MINUTE);

		return minutesSince1970;
	}

	public static Timestamp getTimestampSince1970(int daysSince1970) {
		long millisSince1970 = (long) daysSince1970 * (long) MINUTES_IN_DAY * MILLS_IN_MINUTE;

		return new Timestamp(millisSince1970);
	}

	public static int getMinutesSince1970(long millisSince1970) {
		return (int) (millisSince1970 / MILLS_IN_MINUTE);
	}

	private static final long NANOS_IN_SECOND = 1000000000L;

	public static int getNanoElapsedSecs(long startNanoTime) {
		long currNanoTime = System.nanoTime();

		return getNanoElapsedSecs(currNanoTime, startNanoTime);
	}

	public static int getNanoElapsedSecs(long currNanoTime, long startNanoTime) {
		long elapsedTime = currNanoTime - startNanoTime;

		long elapsedTimeSec = elapsedTime / NANOS_IN_SECOND;

		return (int) elapsedTimeSec;
	}

	public static int getTimestampElapsedSecs(Timestamp currTime, Timestamp startTime) {
		long elapsedTimeMillis = currTime.getTime() - startTime.getTime();

		long elapsedTimeSec = elapsedTimeMillis / 1000L;

		// Assume it always has to be positive.
		if (elapsedTimeSec < 0) {
			elapsedTimeSec = 0;
		}

		return (int) elapsedTimeSec;
	}

	public static final long SECONDS_IN_DAY = (60 * 60 * 24);

	public static Time getMinuteOffsetFromDay(Timestamp currTime, int daySince1970) {
		long currTimeMillis = currTime.getTime();

		long baseMillisSince1970 = (long) daySince1970 * (SECONDS_IN_DAY * 1000);

		currTimeMillis -= baseMillisSince1970;

		// Strip off milliseconds and seconds.

		currTimeMillis /= 60000L;

		return new Time(currTimeMillis * 60000L);
	}

	// Calculate an offset from a timestamp in days.

	public static long getDayOffsetFromTime(long currentTimeMillis, int dayOffset) {
		long dayOffsetMillis = (long) dayOffset * (long) MINUTES_IN_DAY * MILLS_IN_MINUTE;

		return currentTimeMillis + dayOffsetMillis;
	}

	public static Timestamp roundTimeToMinutes(Timestamp currTime) {
		long currTimeMillis = currTime.getTime();

		// Strip off milliseconds and seconds.

		currTimeMillis /= 60000L;

		return new Timestamp(currTimeMillis * 60000L);
	}

	public static String shiftDateString(String dateString, int numDays) {
		Date dateValue = Date.valueOf(dateString);

		long dateTime = dateValue.getTime();

		// Shift by milliseconds.
		dateTime += ((long) numDays * SECONDS_IN_DAY * 1000);

		Timestamp shiftedTime = new Timestamp(dateTime);

		return toDateString(shiftedTime);
	}

	public static String toDateString(Timestamp currDay) {
		int year = currDay.getYear() + 1900;
		int month = currDay.getMonth() + 1;
		int day = currDay.getDate();

		String monthStr;

		if (month < 10) {
			monthStr = "0" + month;
		} else {
			monthStr = "" + month;
		}

		String dayStr;

		if (day < 10) {
			dayStr = "0" + day;
		} else {
			dayStr = "" + day;
		}

		return "" + year + "-" + monthStr + "-" + dayStr;
	}

	private static final long NANOS_IN_MILLI = 1000000L;

	public static int getNanoElapsedMilli(long startNanoTime) {
		long currNanoTime = System.nanoTime();

		return getNanoElapsedMilli(currNanoTime, startNanoTime);
	}

	public static int getNanoElapsedMilli(long currNanoTime, long startNanoTime) {
		long elapsedTime = currNanoTime - startNanoTime;

		long elapsedTimeSec = elapsedTime / NANOS_IN_MILLI;

		return (int) elapsedTimeSec;
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, String param1) {
		return fmt(origMsg, param1, null, null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, String param1, String param2) {
		return fmt(origMsg, param1, param2, null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, String param1, int param2) {
		return fmt(origMsg, param1, Integer.toString(param2), null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, int param1, String param2) {
		return fmt(origMsg, Integer.toString(param1), param2, null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, int param1, int param2) {
		return fmt(origMsg, Integer.toString(param1), Integer.toString(param2), null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, int param1) {
		return fmt(origMsg, Integer.toString(param1), null, null);
	}

	/**
	 * Overloaded format method.
	 */

	public static String fmt(String origMsg, int param1, int param2, int param3) {
		return fmt(origMsg, Integer.toString(param1), Integer.toString(param2), Integer.toString(param3));
	}

	/**
	 * This is our general formatting routine for error messages using %1 %2 %3.
	 * This allows us to embed parameters within the message. We assume that all
	 * values are strings, and use various overloaded methods to convert them from
	 * integers or other types.
	 *
	 * @param origMsg
	 *            The display message.
	 * @param param1
	 *            The first replacement parameter or null if N/A.
	 * @param param2
	 *            The second replacement parameter or null if N/A.
	 * @param param3
	 *            The third replacement parameter or null if N/A.
	 * @return The reformatted string.
	 */

	public static String fmt(String origMsg, String param1, String param2, String param3) {
		// Build array of strings to make life easier.

		String[] replaceStrs = new String[3];

		replaceStrs[0] = param1;
		replaceStrs[1] = param2;
		replaceStrs[2] = param3;

		// Look for replacement %N in the string.

		int percentIdx = 0;

		while ((percentIdx = origMsg.indexOf('%', percentIdx)) > 0) {
			if (percentIdx < origMsg.length() - 1) {
				// Convert to integer value the easy way.

				int numVal = (int) (origMsg.charAt(percentIdx + 1) - '1');

				// Validate range of integer value is within our string array bounds.

				if (numVal >= 0 && numVal < replaceStrs.length && replaceStrs[numVal] != null) {
					// Replace the %N with the appropriate value.

					if (percentIdx == origMsg.length() - 2) {
						// For some reason substring throws a null pointer if I give it a value at the
						// end of the string.

						origMsg = origMsg.substring(0, percentIdx) + replaceStrs[numVal];
					} else {
						origMsg = origMsg.substring(0, percentIdx) + replaceStrs[numVal]
								+ origMsg.substring(percentIdx + 2);
					}
				}
			}

			// Make sure we never see the same value.

			percentIdx++;
		}

		return origMsg;
	}

	public static char[] binToHexChar(byte[] binData) {
		return binToHexChar(binData, 0, binData.length);
	}

	public static char[] binToHexChar(byte[] binData, int offset, int length) {
		char[] hexData = new char[length * 2];

		int iHex = 0;

		for (int iByte = 0; iByte < length; iByte++) {
			int highNibble = (binData[iByte + offset] >> 4) & 0xf;
			int lowNibble = (binData[iByte + offset]) & 0xf;

			hexData[iHex++] = (char) HEX_VALUE[highNibble];
			hexData[iHex++] = (char) HEX_VALUE[lowNibble];
		}

		return hexData;
	}

	public static byte[] binToHexByte(byte[] binData) {
		byte[] hexData = new byte[binData.length * 2];

		int iHex = 0;

		for (int iByte = 0; iByte < binData.length; iByte++) {
			int highNibble = (binData[iByte] >> 4) & 0xf;
			int lowNibble = (binData[iByte]) & 0xf;

			hexData[iHex++] = (byte) HEX_VALUE[highNibble];
			hexData[iHex++] = (byte) HEX_VALUE[lowNibble];
		}

		return hexData;
	}

	public static byte[] hexToBinary(String hexData) {
		int respIdx = 0;
		int lineLen = hexData.length();
		byte[] destBinary = new byte[lineLen / 2];

		for (int iChar = 0; iChar < lineLen - 1 && respIdx < destBinary.length; iChar += 2) {
			byte highNibble = fromHex(hexData.charAt(iChar));
			byte lowNibble = fromHex(hexData.charAt(iChar + 1));

			destBinary[respIdx++] = (byte) ((highNibble << 4) | lowNibble);
		}

		return destBinary;
	}

	public static char[] bytePadToChar(byte[] byteArray) {
		int respIdx = 0;
		int lineLen = byteArray.length;
		int byteIdx = 0;

		// Trim zero padding at end.
		while (lineLen > 1 && byteArray[lineLen - 1] == 0 && byteArray[lineLen - 2] == 0) {
			lineLen -= 2;
		}

		char[] destChars = new char[lineLen / 2];

		for (int iChar = 0; iChar < destChars.length; iChar++) {
			byte highNibble = byteArray[byteIdx++];
			byte lowNibble = byteArray[byteIdx++];

			destChars[iChar] = (char) (((char) highNibble << 8) | (char) lowNibble);
		}

		return destChars;
	}

	public static byte[] charToBytePad(char[] charArray) {
		int byteLen = charArray.length * 2;
		int byteIdx = 0;

		// Add padding to round result to multiple of 16.
		byteLen = ((byteLen + 15) / 16) * 16;

		// Also add verification string.

		int verifyIdx = byteLen;

		byteLen += 16;

		byte[] destBytes = new byte[byteLen];

		for (int iChar = 0; iChar < charArray.length; iChar++) {
			byte highNibble = (byte) (charArray[iChar] >> 8);
			byte lowNibble = (byte) charArray[iChar];

			destBytes[byteIdx++] = highNibble;
			destBytes[byteIdx++] = lowNibble;
		}

		// Append verification string.

		for (int iVerify = 0; iVerify < 16; iVerify++) {
			destBytes[verifyIdx + iVerify] = (byte) VERIFICATION_STRING.charAt(iVerify);
		}

		return destBytes;
	}

	public static byte fromHex(char hexDigit) {
		if (hexDigit >= '0' && hexDigit <= '9') {
			return (byte) (hexDigit - '0');
		} else if (hexDigit >= 'A' && hexDigit <= 'F') {
			return (byte) ((hexDigit - 'A') + 10);
		} else if (hexDigit >= 'a' && hexDigit <= 'f') {
			return (byte) ((hexDigit - 'a') + 10);
		} else {
			return 0;
		}
	}

	public static int from2DigitString(String twoDigitInt) {
		char tensDigit = twoDigitInt.charAt(0);
		char onesDigit = twoDigitInt.charAt(1);

		int returnValue = 0;

		returnValue = (int) (tensDigit - '0') * 10;
		returnValue += (int) (onesDigit - '0');

		return returnValue;
	}

	public static int byteToInt(byte[] byteData, int offset) {
		int returnValue;

		returnValue = ((int) byteData[offset] & 0xff);
		returnValue |= (((int) byteData[offset + 1] & 0xff) << 8);
		returnValue |= (((int) byteData[offset + 2] & 0xff) << 16);
		returnValue |= (((int) byteData[offset + 3] & 0xff) << 24);

		return returnValue;
	}

	public static int byteCastInt(byte byteData) {
		int returnValue;

		returnValue = ((int) byteData & 0xff);

		return returnValue;
	}

	public static short byteCastShort(byte byteData) {
		short returnValue;

		returnValue = (short) ((int) byteData & 0xff);

		return returnValue;
	}

	public static int shortCastInt(short shortData) {
		int returnValue;

		returnValue = ((int) shortData & 0xffff);

		return returnValue;
	}

	public static short byteToShort(byte[] byteData, int offset) {
		short returnValue;

		returnValue = (short) ((int) byteData[offset] & 0xff);
		returnValue |= (short) (((int) byteData[offset + 1] & 0xff) << 8);

		return returnValue;
	}

	public static void intToByte(int intValue, byte[] returnData, int offset) {
		returnData[offset] = (byte) intValue;
		returnData[offset + 1] = (byte) (intValue >> 8);
		returnData[offset + 2] = (byte) (intValue >> 16);
		returnData[offset + 3] = (byte) (intValue >> 24);
	}

	public static void shortToByte(short shortValue, byte[] returnData, int offset) {
		returnData[offset] = (byte) shortValue;
		returnData[offset + 1] = (byte) (shortValue >> 8);
	}
}
