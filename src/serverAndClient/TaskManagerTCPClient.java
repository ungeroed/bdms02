package serverAndClient;

import java.io.*;
import java.io.*;
import java.net.*;

import xml.Task;

public class TaskManagerTCPClient {
	
	public TaskManagerTCPClient(){
		demand();
	}
	
	public void demand()
	{
		try {
			int serverPort = 7890;						
			InetAddress serverAddress = InetAddress.getByName("localhost");
			Socket transmitterSocket = new Socket(serverAddress, serverPort);
			transmitterSocket.setReuseAddress(true);
						
			OutputStream os = transmitterSocket.getOutputStream();
			ObjectOutputStream dos = new ObjectOutputStream(os);
						
			InputStream is = transmitterSocket.getInputStream();
			ObjectInputStream inStream = new ObjectInputStream(is);
	
			String command = "get";
			dos.writeUTF(command);
			dos.flush();
			System.out.println("Client here: " + command + "transmitted");
		
			String ping = inStream.readUTF();
			if (ping.equalsIgnoreCase("get")) {
				System.out.println("Client here: ping received");			
				dos.writeUTF("rao");
				dos.flush();
				Task task = (Task)inStream.readObject();
			}
			transmitterSocket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TaskManagerTCPClient client = new TaskManagerTCPClient();		
	}

}
