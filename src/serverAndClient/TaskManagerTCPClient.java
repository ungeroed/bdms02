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
			InetAddress serverAddress = InetAddress.getByName("localhost");
			int serverPort = 7890;
			String command = "get";
			Socket socket = new Socket(serverAddress, serverPort);
			OutputStream os = socket.getOutputStream();
			ObjectOutputStream dos = new ObjectOutputStream(os);
			dos.writeUTF(command);
			dos.flush();
			
			InputStream is = socket.getInputStream();
			ObjectInputStream inStream = new ObjectInputStream(is);
			String ping = inStream.readUTF();
			
			dos.writeUTF("rao");
			Task task = (Task)inStream.readObject();
			
			socket.close();
			
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
