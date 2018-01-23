package com.dowon.chatting;

import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Server extends JFrame{

	private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	//constructor
	public Server() {
		super("Dowons Instant Messenger");
		userText = new JTextField();
		userText.setEditable(false);
		userText.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
				);
		add(userText, BorderLayout.NORTH);	
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(400,200);
		setVisible(true);
	}
	
	//set up and run the server
	public void startRunning() {
		try {
			server = new ServerSocket(6789, 100);//뒤에 100은 접속 가능한 클라이언트 수라고 볼 수 있다.
			while(true) {
				try {
					waitForConnection();
					setupStreams();
					whileChatting();
					//connection and have conversation
				}catch(EOFException e){
					showMessage("\n Server ended the connection!");//내가 나갔다는 것을 보여준다.
					
				}finally {
					closeCrap();
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//wait for connection , then display connection information
	
	private void waitForConnection() throws IOException{
		showMessage("Waiting for someone to connect....\n ");
		connection = server.accept();
		showMessage("Now connected to " + connection.getInetAddress().getHostName());
	}
	//get stream to send and receive data
	private void setupStreams() throws IOException{
		output = new ObjectOutputStream(connection.getOutputStream());//다른 컴퓨터에 연결하는 것, 나가는 PATH
		output.flush();//버퍼에 바이트가 가끔 남아있을 때,,,, 나가는 부분만 flush할 수 있다.
		input = new ObjectInputStream(connection.getInputStream());//들어오는 PATH
		showMessage("\n Streams are now setup! \n");	
	}
	//during the chat conversation
	private void whileChatting() throws IOException{
		String message = " You are now connected! ";
		sendMessage(message);
		ableToType(true);
		do {
			try {
				message = (String) input.readObject();
				showMessage("\n" + message);
			}catch (ClassNotFoundException e){
				showMessage("\n idk wtf that user send!");				
			}
			//have a conversation
		}while(!message.equals("CLIENT - END"));
	}
	// Closing down the Streams and sockets
	private void closeCrap() {
		showMessage("\n Closing connections...\n");
		ableToType(false);
		try {
			output.close();
			input.close();
			connection.close();
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	// send a message to client
	private void sendMessage(String message) {
		try {
			output.writeObject("SERVER - " + message);
			output.flush();
			showMessage("\nSERVER - " + message);
		}catch(IOException e){
			chatWindow.append("\n ERROR: Dude i cant send that message");
		}
	}
	
	//update chatWindow
	private void showMessage(final String text) {
		SwingUtilities.invokeLater(
				new Runnable() { //쓰레드 생성
					public void run() {
						chatWindow.append(text);
					}
				}
			);
		
	}
	// let the user type stuff into their box
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(
				new Runnable() { //쓰레드 생성
					public void run() {
						userText.setEditable(tof);
					}
				}
			);		
		
	}
}



















