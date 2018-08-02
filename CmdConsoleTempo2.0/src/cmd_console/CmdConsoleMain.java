package cmd_console;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.ParallelGroup;
import javax.swing.GroupLayout.SequentialGroup;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.LayoutStyle;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

public class CmdConsoleMain extends JFrame implements DocumentListener, ConsoleCallBackInterface {

	private GatewayConsoleConnection mGatewayConsoleConnection = null;
	private boolean ConnEstablished = false;

	private JTextField entry;
	private JLabel jLabel1;
	private JScrollPane jScrollPane1;
	private JLabel status;
	private JTextArea textArea;

	final static int TEMPO_ID = 10503;

	final static Color HILIT_COLOR = Color.LIGHT_GRAY;
	final static Color ERROR_COLOR = Color.PINK;
	final static String CANCEL_ACTION = "cancel-search";
	final static String ENTER_ACTION = "execute-command";

	final Color entryBg;
	final Highlighter hilit;
	final Highlighter.HighlightPainter painter;

	public CmdConsoleMain() {
		initComponents();

		InputStream in = getClass().getResourceAsStream("CpCommandSet.txt");
		try {
			textArea.read(new InputStreamReader(in), null);
		} catch (IOException e) {
			e.printStackTrace();
		}

		hilit = new DefaultHighlighter();
		painter = new DefaultHighlighter.DefaultHighlightPainter(HILIT_COLOR);
		textArea.setHighlighter(hilit);

		entryBg = entry.getBackground();
		entry.getDocument().addDocumentListener(this);

		InputMap im = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap am = entry.getActionMap();
		im.put(KeyStroke.getKeyStroke("ESCAPE"), CANCEL_ACTION);
		am.put(CANCEL_ACTION, new CancelAction());

		InputMap MyInputKey = entry.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		ActionMap MyKeyAction = entry.getActionMap();
		MyInputKey.put(KeyStroke.getKeyStroke("ENTER"), ENTER_ACTION);
		MyKeyAction.put(ENTER_ACTION, new EnterAction());

		InitConsoleConn();
	}

	public void InitConsoleConn() {
		try {
			mGatewayConsoleConnection = CpGatewayConnectivity.InitializeConnection(TEMPO_ID, this);
		} catch (Exception e) {
			System.out.println("Exception thrown in the parent class");
		}
	}

	public void gatewayResponseAvailable(GatewayResponse gatewayResponse) {
		switch (gatewayResponse.getMessageType()) {
		case GatewayResponse.GR_TYPE_STREAM_IN_USE:
			System.out.println("GR_TYPE_STREAM_IN_USE");
			message("Stream is alreay in use.");
			break;

		case GatewayResponse.GR_TYPE_STREAM_WAITING:
			System.out.println("GR_TYPE_STREAM_WAITING");
			message("Stream is waiting..");
			break;

		case GatewayResponse.GR_TYPE_STREAM_ESTABLISHED:
			System.out.println("GR_TYPE_STREAM_ESTABLISHED");
			message("Stream established");
			ConnEstablished = true;
			break;

		case GatewayResponse.GR_TYPE_STREAM_CLOSING:
			System.out.println("GR_TYPE_STREAM_CLOSING");
			break;

		case GatewayResponse.GR_TYPE_CONSOLE_DATA:
			// System.out.println("GR_TYPE_CONSOLE_DATA");
			String ParsedString = new String(gatewayResponse.getMessagePayload(), Charset.forName("UTF-8"));
			textArea.append(ParsedString + "\n\r");
			break;

		default:
			System.out.println("Response type: " + gatewayResponse.getMessageType());
			message("Response type: " + gatewayResponse.getMessageType() + "UNKNOWN");
			break;
		}
	}

	public void networkException(String message, Exception ex) {
		System.out.println(message + " " + ex.getMessage());
	}

	public void connectionClosing() {
		System.out.println("Connection is closed by the Server");
		mGatewayConsoleConnection = null;
		ConnEstablished = false;
		message("Connection is closed by the Server! Close and re-open the window to continue..");
	}

	// Close our stream connection if it's open. Synchronize this so other threads
	// don't write
	// to the stream while we're closing it.

	synchronized public void closeGatewayStream() {
		if (mGatewayConsoleConnection != null) {
			// Notify peer of the stream shutdown.
			mGatewayConsoleConnection.sendGatewayStreamRequest(new GatewayMessage(GatewayMessage.GM_TYPE_STOP_STREAM));

			try {
				Thread.sleep(500);
			} catch (Exception ex) {
				System.out.println("Got exception on closing connection");
			}

			mGatewayConsoleConnection.close();
			mGatewayConsoleConnection = null;
			ConnEstablished = false;
		}
	}

	/**
	 * This method is called from within the constructor to initialize the form.
	 */

	class CancelAction extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			hilit.removeAllHighlights();
			entry.setText("");
			entry.setBackground(entryBg);
			closeGatewayStream();
		}
	}

	class EnterAction extends AbstractAction {
		public void actionPerformed(ActionEvent ev) {
			String s = entry.getText();
			if (ConnEstablished == true) {
				textArea.setText("Executed Command: <" + s + " >\n\n\r");
				mGatewayConsoleConnection.sendGatewayStreamRequest(
						new GatewayMessage(GatewayMessage.GM_TYPE_CONSOLE_DATA, s.getBytes()));
			} else {
				message("Stream is not yet established");
			}
		}
	}

	private void initComponents() {
		entry = new JTextField();
		textArea = new JTextArea();
		status = new JLabel();
		jLabel1 = new JLabel();

		/* For auto scrolling */
		DefaultCaret caret = (DefaultCaret) textArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setTitle("CarePredict Tempo2.0 Command Console");

		textArea.setColumns(20);
		textArea.setLineWrap(true);
		textArea.setRows(5);
		textArea.setWrapStyleWord(true);
		textArea.setEditable(false);
		jScrollPane1 = new JScrollPane(textArea);

		jLabel1.setText("Enter command to execute:");

		GroupLayout layout = new GroupLayout(getContentPane());
		getContentPane().setLayout(layout);

		// Create a parallel group for the horizontal axis
		ParallelGroup hGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);

		// Create a sequential and a parallel groups
		SequentialGroup h1 = layout.createSequentialGroup();
		ParallelGroup h2 = layout.createParallelGroup(GroupLayout.Alignment.TRAILING);

		// Add a container gap to the sequential group h1
		h1.addContainerGap();

		// Add a scroll pane and a label to the parallel group h2
		h2.addComponent(jScrollPane1, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);
		h2.addComponent(status, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE);

		// Create a sequential group h3
		SequentialGroup h3 = layout.createSequentialGroup();
		h3.addComponent(jLabel1);
		h3.addPreferredGap(LayoutStyle.ComponentPlacement.RELATED);
		h3.addComponent(entry, GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE);

		// Add the group h3 to the group h2
		h2.addGroup(h3);
		// Add the group h2 to the group h1
		h1.addGroup(h2);

		h1.addContainerGap();

		// Add the group h1 to the hGroup
		hGroup.addGroup(GroupLayout.Alignment.TRAILING, h1);
		// Create the horizontal group
		layout.setHorizontalGroup(hGroup);

		// Create a parallel group for the vertical axis
		ParallelGroup vGroup = layout.createParallelGroup(GroupLayout.Alignment.LEADING);
		// Create a sequential group v1
		SequentialGroup v1 = layout.createSequentialGroup();
		// Add a container gap to the sequential group v1
		v1.addContainerGap();
		// Create a parallel group v2
		ParallelGroup v2 = layout.createParallelGroup(GroupLayout.Alignment.BASELINE);
		v2.addComponent(jLabel1);
		v2.addComponent(entry, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE);
		// Add the group v2 tp the group v1
		v1.addGroup(v2);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, 233, Short.MAX_VALUE);
		v1.addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED);
		v1.addComponent(status);
		v1.addContainerGap();

		// Add the group v1 to the group vGroup
		vGroup.addGroup(v1);
		// Create the vertical group
		layout.setVerticalGroup(vGroup);
		pack();
	}

	public void search() {
		hilit.removeAllHighlights();

		String s = entry.getText();
		if (s.length() <= 0) {
			// message("Nothing to search");
			return;
		}

		String content = textArea.getText();
		int index = content.indexOf(s, 0);
		if (index >= 0) { // match found
			try {
				int end = index + s.length();
				hilit.addHighlight(index, end, painter);
				textArea.setCaretPosition(end);
				entry.setBackground(entryBg);
				// message("'" + s + "' found. Press ESC to end search");
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		} else {
			entry.setBackground(ERROR_COLOR);
			// message("'" + s + "' not found. Press ESC to start a new search");
		}
	}

	void message(String msg) {
		status.setText(msg);
	}

	// DocumentListener methods
	public void insertUpdate(DocumentEvent ev) {
		search();
	}

	public void removeUpdate(DocumentEvent ev) {
		InputStream in = getClass().getResourceAsStream("CpCommandSet.txt");
		try {
			textArea.read(new InputStreamReader(in), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
		search();
	}

	public void changedUpdate(DocumentEvent ev) {
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Starting CarePredict Tempo2.0 Command Console..");

		// Schedule a job for the event dispatch thread:
		// creating and showing this application's GUI.
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				// Turn off metal's use of bold fonts
				UIManager.put("swing.boldMetal", Boolean.FALSE);
				new CmdConsoleMain().setVisible(true);
			}
		});
	}
}
