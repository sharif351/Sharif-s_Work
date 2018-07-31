
public class CpGatewayConnectivity extends Thread {

	private final static String DEFAULT_GATEWAY_IP = "gateway.carepredict.com";
	private final static int DEFAULT_GATEWAY_PORT = 443;

	private boolean isGateWayConnacted = false;
	private boolean isStartConsoleMode = false;

	public CpGatewayConnectivity() {
		System.out.println("CpGatewayConnectivity class started:");
		Connect2CpGateWay();
	}

	private void Connect2CpGateWay() {
		System.out.println("Connectiong to CarePredict GateWay");
		isGateWayConnacted = true;
	}

	public void StartTempoConsole() {

	}

	public void PrintMyCommand(String s) {
		System.out.println("Passed command is: " + s);
	}
}
