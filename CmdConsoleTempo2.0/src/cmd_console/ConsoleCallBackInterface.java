package cmd_console;

public interface ConsoleCallBackInterface {
	void gatewayResponseAvailable(GatewayResponse gatewayResponse);

	// Handle our network exceptions for display at the UI layer.

	void networkException(String message, Exception ex);

	void connectionClosing();
}
