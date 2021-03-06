package main;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Chat {
	
	public enum Sender {User, Bot, Error}
	
	private static JFrame frame;
	private static JPanel panel;
	private static JPanel panel2;
	private static JTextArea textArea;
	private static SpeechRecognizeEvent EVENT;
	private static JTextField inputField;
	
	public static void init(SpeechRecognizeEvent event) {
		createFrame();
		Chat.EVENT = event;
	}
	
	private static void createFrame() {
		frame = new JFrame();
		frame.setTitle("Chat");
		frame.setLocationByPlatform(true);
		frame.setSize(550, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		frame.add(panel);
		
		panel2 = new JPanel();
		panel2.setLayout(new BorderLayout());
		panel.add(panel2, BorderLayout.PAGE_END);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setFont(new Font("Arial", Font.PLAIN, 15));
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
		
		JScrollPane textAreaScrollPane = new JScrollPane(textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		textAreaScrollPane.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
			public void adjustmentValueChanged(AdjustmentEvent e) {
				e.getAdjustable().setValue(e.getAdjustable().getMaximum());
			}
		});
		panel.add(textAreaScrollPane, BorderLayout.CENTER);
		
		inputField = new JTextField();
		inputField.setFont(new Font("Arial", Font.PLAIN, 32));
		inputField.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				processInput();
			}
		});
		panel2.add(inputField, BorderLayout.CENTER);
		
		JButton sendButton = new JButton("     \u25BA     ");
		sendButton.setFocusable(false);
		sendButton.setToolTipText("Send Message");
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				processInput();
			}
		});
		panel2.add(sendButton, BorderLayout.LINE_END);
		
		frame.setVisible(true);
	}
	
	public static void send(Sender sender, String message) {
		textArea.append(sender.toString() +  ": " + message + System.lineSeparator());
	}
	
	private static void processInput() {
		String input = inputField.getText();
		inputField.setText("");
		textArea.update(textArea.getGraphics());
		inputField.update(inputField.getGraphics());
		EVENT.say(input);
		
	}
	
	public static void sendError(Exception e) {
		Chat.send(Sender.Error, e.getClass().getSimpleName() + " -> " + e.getMessage());
		e.printStackTrace();
	}
	
}
