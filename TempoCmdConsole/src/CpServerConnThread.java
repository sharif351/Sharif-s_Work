
public class CpServerConnThread extends Thread {

	private final static String DEFAULT_GATEWAY_IP = "gateway.carepredict.com";
	private final static int DEFAULT_GATEWAY_PORT = 443;

	private boolean isGateWayConnacted = false;

	public CpServerConnThread() {
		System.out.println("CpGatewayConnectivity class started:");

	}

}
