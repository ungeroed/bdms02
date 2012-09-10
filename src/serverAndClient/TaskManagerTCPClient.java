package serverAndClient;

import java.io.*;
import java.io.*;
import java.net.*;

public class TaskManagerTCPClient {
	
	public TaskManagerTCPClient(){
		demand();
	}
	
	public void demand()
	{
		try {
			InetAddress serverAddress = InetAddress.getByName("localhost");
			int serverPort = 7896;
			String message = "A secret message.";
			Socket socket = new Socket(serverAddress, serverPort);
			OutputStream os = socket.getOutputStream();
			DataOutputStream dos = new DataOutputStream(os);
			dos.writeUTF(message);
			dos.flush();
			socket.close();
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
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
